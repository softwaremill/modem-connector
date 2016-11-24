package com.sml.pwsat.modem.demodulator

import com.sml.pwsat.modem.demodulator.DemodulatorState.DemodulatorState
import com.sml.pwsat.modem.packet.{AX25Packet, PacketHandler}


class AFSK1200Demodulator(sample_rate: Int, filter_length: Int, emphasis: Int, handler: PacketHandler) {

  private val Emphasis0: Int = 0
  private val Emphasis6: Int = 6
  private val InterpolateRate: Int = 8000
  private val StandardRate: Int = 8000

  private var td_filter: Array[Float] = _
  private var cd_filter: Array[Float] = _
  private var samples_per_bit: Float = _


  private var c0_real: Array[Float] = _

  private var c0_imag: Array[Float] = _

  private var c1_real: Array[Float] = _

  private var c1_imag: Array[Float] = _
  private var diff: Array[Float] = _
  private var previous_fdiff: Float = _
  private var last_transition: Int = _
  private var data: Int = _

  private var bitcount: Int = _
  private var phase_inc_f0: Float = _

  private var phase_inc_f1: Float = _
  private var packetOpt: Option[AX25Packet] = None

  private var state: DemodulatorState = DemodulatorState.WAITING
  private var interpolate: Boolean = false
  private var interpolate_last: Float = _
  private var interpolate_original: Boolean = false
  /*
     * Diagnostic variables for estimating packet quality
   */
  private var f0_period_count: Int = 0

  private var f1_period_count: Int = 0
  private var f0_max: Float = 0

  private var f1_min: Float = 0
  // to collect average max, min in the filtered diff signal
  private var f0_current_max: Float = 0

  private var f1_current_min: Float = 0
  private var max_period_error: Float = 0

  //for addSample
  private var j_td: Int = 0
  // time domain index
  private var j_cd: Int = 0
  private var j_corr: Int = 0
  // correlation index
  private var phase_f0: Float = 0

  private var phase_f1: Float = 0
  private var t: Int = 0
  // running sample counter
  private var flag_count: Int = 0
  private var flag_separator_seen: Boolean = false

  @volatile private var data_carrier: Boolean = false

  init(sample_rate, filter_length, emphasis, handler)

  private def statisticsInit() {
    f0_period_count = 0
    f1_period_count = 0
    f0_max = 0.0f
    f1_min = 0.0f
    max_period_error = 0.0f
  }

  private def statisticsFinalize() {
    f0_max = f0_max / f0_period_count
    f1_min = f1_min / f1_period_count
  }

  private def calcSampleRate(sr: Int): Int = {
    if (sr == InterpolateRate) {
      interpolate = true
      StandardRate
    } else {
      sr
    }
  }

  def init(sample_rate: Int, filter_length: Int, emphasis: Int, handler: PacketHandler) {
    val sampleRate: Int = calcSampleRate(sample_rate)
    val rate_index: Int = Afsk1200Filters.sample_rates.toSeq.indexOf(sampleRate)
    samples_per_bit = (sampleRate / 1200.0).toFloat

    printf("samples per bit = %.3f\n", samples_per_bit)

    val tdf = emphasis match {
      case Emphasis0 => Afsk1200Filters.time_domain_filter_none
      case Emphasis6 => Afsk1200Filters.time_domain_filter_full
      case _ =>
        printf("Filter for de-emphasis of %ddB is not availabe, using 6dB\n", emphasis)
        Afsk1200Filters.time_domain_filter_full
    }

    var filter_index: Int = 0
    for (i <- tdf.indices) {
      printf("Available filter length %d\n", tdf(i)(rate_index).length)
      filter_index += 1
      if (filter_length == tdf(i)(rate_index).length) {
        printf("Using filter length %d\n", filter_length)
        filter_index = i
      }
    }

    if (filter_index == tdf.length) {
      filter_index = tdf.length - 1
      printf("Filter length %d not supported, using length %d\n", filter_length, tdf(filter_index)(rate_index).length)
    }

    td_filter = tdf(filter_index)(rate_index)
    cd_filter = Afsk1200Filters.corr_diff_filter(filter_index)(rate_index)

    val cArraySize: Int = Math.floor(samples_per_bit).toInt
    c0_real = new Array[Float](cArraySize)
    c0_imag = new Array[Float](cArraySize)
    c1_real = new Array[Float](cArraySize)
    c1_imag = new Array[Float](cArraySize)
    diff = new Array[Float](cd_filter.length)

    phase_inc_f0 = (2.0 * Math.PI * 1200.0 / sampleRate).toFloat
    phase_inc_f1 = (2.0 * Math.PI * 2200.0 / sampleRate).toFloat
  }

  def prepareSample(s: Float): Float = {
    if (interpolate) {
      if (interpolate_original) {
        interpolate_last = s
        interpolate_original = false
        s
      } else {
        interpolate_original = true
        0.5f * (s + interpolate_last)
      }
    } else {
      s
    }
  }

  def calculateFDiff(s: Float): Float = {
    val u1: Array[Float] = new Array[Float](td_filter.length)
    u1(j_td) = prepareSample(s)

    val x: Array[Float] = new Array[Float](td_filter.length)
    x(j_td) = Filter.filter(u1, j_td, td_filter)

    c0_real(j_corr) = x(j_td) * Math.cos(phase_f0).toFloat
    c0_imag(j_corr) = x(j_td) * Math.sin(phase_f0).toFloat

    c1_real(j_corr) = x(j_td) * Math.cos(phase_f1).toFloat
    c1_imag(j_corr) = x(j_td) * Math.sin(phase_f1).toFloat

    phase_f0 += phase_inc_f0
    if (phase_f0 > 2.0 * Math.PI) phase_f0 -= (2.0 * Math.PI).toFloat
    phase_f1 += phase_inc_f1
    if (phase_f1 > 2.0 * Math.PI) phase_f1 -= (2.0 * Math.PI).toFloat

    var cr: Float = sum(c0_real)
    var ci: Float = sum(c0_imag)
    val c0: Float = Math.sqrt(cr * cr + ci * ci).toFloat

    cr = sum(c1_real)
    ci = sum(c1_imag)
    val c1: Float = Math.sqrt(cr * cr + ci * ci).toFloat

    diff(j_cd) = c0 - c1
    val fdiff: Float = Filter.filter(diff, j_cd, cd_filter)
    fdiff
  }

  def collectStatistics(fdiff: Float, p: Int, bits: Int): Unit = {
    if (fdiff < 0) {
      // last period was high, meaning f0
      f0_period_count += 1
      f0_max += f0_current_max
      val err: Double = Math.abs(bits - (p / samples_per_bit))
      if (err > max_period_error) max_period_error = err.toFloat
      // prepare for the period just starting now
      f1_current_min = fdiff
    } else {
      f1_period_count += 1
      f1_min += f1_current_min
      val err: Double = Math.abs(bits - (p / samples_per_bit)).toDouble
      if (err > max_period_error) max_period_error = err.toFloat
      // prepare for the period just starting now
      f0_current_max = fdiff
    }
  }

  def decoding(bits: Int): Unit = {
    if (bits == 7) {
      if (packetOpt.isDefined && packetOpt.get.terminate()) {
        statisticsFinalize()
        handler.handlePacket(packetOpt.get.bytesWithoutCRC())
      }
      packetOpt = None
      state = DemodulatorState.JUST_SEEN_FLAG
    } else {
      if (bits != 1) {
        flag_count = 0
      } else {
        if (flag_count > 0 && !flag_separator_seen) {
          flag_separator_seen = true
        }
        else {
          flag_count = 0
        }
      }
      for (k <- Range(0, bits - 1)) {
        bitcount += 1
        data >>= 1
        data += 128
        createPacket()
      }
      if (bits - 1 != 5) {
        // the zero after the ones is not a stuffing
        bitcount += 1
        data >>= 1
        createPacket()
      }
    }
  }

  def waiting(bits: Int): Unit = {
    if (bits == 7) {
      state = DemodulatorState.JUST_SEEN_FLAG
      data_carrier = true
      statisticsInit(); // start measuring a new packet
    }
  }

  def justSeenFlag(bits: Int): Unit = {
    if (bits != 7) {
      state = DemodulatorState.DECODING
    }
  }

  def switchState(bits: Int): Unit = {
    if (bits == 7) {
      state match {
        case DemodulatorState.WAITING => waiting(bits)
        case DemodulatorState.JUST_SEEN_FLAG => justSeenFlag(bits)
        case DemodulatorState.DECODING => decoding(bits)
      }
    } else {
      state match {
        case DemodulatorState.WAITING => waiting(bits)
        case DemodulatorState.JUST_SEEN_FLAG => justSeenFlag(bits)
        case DemodulatorState.DECODING =>
      }
    }
  }

  def calculateState(bits: Int): Unit = {
    if (bits == 0 || bits > 7) {
      state = DemodulatorState.WAITING
      data_carrier = false
      flag_count = 0
    } else {
      if (bits == 7) {
        flag_count += 1
        flag_separator_seen = false
        data = 0
        bitcount = 0
        switchState(bits)
      } else {
        switchState(bits)
        if (state == DemodulatorState.DECODING) {
          decoding(bits)
        }
      }
    }
  }

  def createPacket(): Unit = {
    if (bitcount == 8) {
      if (packetOpt.isEmpty) {
        packetOpt = Some(new AX25Packet())
      }
      if (!packetOpt.get.addByte(data.toByte)) {
        state = DemodulatorState.WAITING
        data_carrier = false
      }
      data = 0
      bitcount = 0
    }
  }

  def addSamples(s: Array[Float], n: Int) = {
    var i: Int = 0
    while (i < n) {
      val fdiff: Float = calculateFDiff(s(i))
      if (!interpolate || !interpolate_original) {
        i += 1
      }

      if (previous_fdiff * fdiff < 0 || previous_fdiff == 0) {
        // we found a transition
        val p: Int = t - last_transition
        last_transition = t
        val bits: Int = Math.round(p / samples_per_bit)

        // collect statistics
        collectStatistics(fdiff, p, bits)

        calculateState(bits)
      }

      previous_fdiff = fdiff

      t += 1

      j_td += 1
      if (j_td == td_filter.length) j_td = 0

      j_cd += 1
      if (j_cd == cd_filter.length) j_cd = 0

      j_corr += 1
      if (j_corr == c0_real.length /* samples_per_bit*/ ) j_corr = 0

    }
  }

  private def sum(x: Array[Float]): Float = {
    x.sum
  }
}
