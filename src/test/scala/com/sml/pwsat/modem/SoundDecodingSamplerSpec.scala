package com.sml.pwsat.modem

import java.io.File
import javax.sound.sampled.{AudioFormat, TargetDataLine}

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}


class SoundDecodingSamplerSpec extends FlatSpec with Matchers with BeforeAndAfter {


  "A SoundDecodingSampler" should "process an Audio File into samples" in {
    val file = new File(getClass.getResource("/packet12.wav").getPath)
    val interface:SoundInterface = SoundInterfaceFactory.apply(SoundInterfaceType.INPUT, "Soundflower (2ch)")
    val line: TargetDataLine = interface.getDataLine.get.asInstanceOf[TargetDataLine]
    val format: AudioFormat = interface.getAudioFormat
//    val sampler: SoundDecodingSampler = new SoundDecodingSampler(line, format)
//    sampler.start()
  }

}
