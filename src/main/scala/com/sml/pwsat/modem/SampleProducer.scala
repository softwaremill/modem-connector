package com.sml.pwsat.modem

import java.util.concurrent.BlockingQueue
import javax.sound.sampled.AudioFormat

import com.typesafe.scalalogging.LazyLogging


abstract class SampleProducer[T](queue: BlockingQueue[T]) extends Runnable with LazyLogging {

  val BitSampleSize16: Int = 16
  val BitSampleSize8: Int = 8

  def produceSample(audioFormat: AudioFormat, buf: Array[Byte], offset: Int, dest: Array[Double], destoff: Int): Int = {
    val sb: Int = audioFormat.getSampleSizeInBits
    val chs: Int = audioFormat.getChannels
    var samp: Int = 0
    var samp1: Int = 0
    var samp2: Int = 0
    var dsamp: Double = 0
    var off = offset
    for (ch <- 1 to chs) {
      sb match {
        case BitSampleSize16 => {
          val res: (Double, Int) = handle16BitSample(audioFormat, buf, off)
          dsamp = res._1
          off = res._2
        }
        case BitSampleSize8 => {
          val res: (Double, Int) = handle8BitSample(audioFormat, buf, off)
          dsamp = res._1
          off = res._2
        }
        case _ => off += 1
      }
    }
    dest(destoff) = dsamp / chs.toDouble
    off
  }

  def handle16BitSample(audioFormat: AudioFormat, buf: Array[Byte], offset: Int): (Double, Int) = {
    val samp1 = buf(offset) & 0xFF
    val samp2 = buf(offset + 1) & 0xFF
    val samp: Int = audioFormat.isBigEndian match {
      case true => {
        audioFormat.getEncoding == AudioFormat.Encoding.PCM_SIGNED match {
          case true if (((samp1 << 8) | samp2) & 0x8000) == 0x8000 => ((samp1 << 8) | samp2) | 0xFFFF0000
          case _ => (samp1 << 8) | samp2
        }
      }
      case false => {
        audioFormat.getEncoding == AudioFormat.Encoding.PCM_SIGNED match {
          case true if (((samp2 << 8) | samp1) & 0x8000) == 0x8000 => ((samp2 << 8) | samp1) | 0xFFFF0000
          case _ => (samp2 << 8) | samp1
        }
      }
    }
    (samp / 32768.0d, offset + 2)
  }

  def handle8BitSample(audioFormat: AudioFormat, buf: Array[Byte], offset: Int): (Double, Int) = {
    audioFormat.getEncoding == AudioFormat.Encoding.PCM_SIGNED && (((buf(offset) & 0xFF) & 0x80) == 0x80) match {
      case true => (((buf(offset) & 0xFF) | 0xFFFFFF00) / 128.0d, offset + 1)
      case false => ((buf(offset) & 0xFF) / 128.0d, offset + 1)
    }
  }

  def process(fltbuf: Array[T]): Unit = {
    fltbuf.foreach(sample => queue.put(sample))
  }
}
