package com.sml.pwsat.modem

import java.util.concurrent.BlockingQueue

import com.sml.pwsat.modem.demodulator.AFSK1200Demodulator
import com.sml.pwsat.modem.packet.PacketHandler

class PrintSampleConsumer(queue: BlockingQueue[Double]) extends SampleConsumer(queue) {

  val handler: PacketHandler = new PacketHandler
  val demodulator: AFSK1200Demodulator = new AFSK1200Demodulator(11025, 1, 0, handler)

  override def consume(sample: Double): Unit = {
//    println("%.9f" format sample)
    /*
    f[0] = Float.parseFloat(l);
                    multi.addSamples(f, 1);
     */
    demodulator.addSample(sample.toFloat)
  }
}
