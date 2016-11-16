package com.sml.pwsat.modem

import javax.sound.sampled.Mixer.Info
import javax.sound.sampled._

import com.sml.pwsat.modem.SoundInterfaceType.SoundInterfaceType
import com.typesafe.scalalogging.LazyLogging

trait SoundInterface extends LazyLogging {
  val sampleRate: Int = 9600
  val sampleSizeInBits: Int = 16
  val channels: Int = 1
  val sampleBytes: Int = 2

  def name: String

  def getDataLine: Option[DataLine] = {
    val fmt: AudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
      sampleRate, sampleSizeInBits, channels, sampleBytes, sampleRate, false)
    SoundInterface.mixers.toSeq.find(_.getName.equalsIgnoreCase(name)).map(mi => getAudioSystemLine(fmt, mi).get)
  }

  protected def getAudioSystemLine(fmt: AudioFormat, mixerInfo: Mixer.Info): Option[DataLine]

  override def toString = "Interface Name: " + name
}

object SoundInterface {
  private val mixers: Array[Mixer.Info] = AudioSystem.getMixerInfo
  def getSoundInterfacesNames:Seq[String] = mixers.map(_.getName).toSeq
}

private class InputSoundInterface(val name: String) extends SoundInterface {
  override def getAudioSystemLine(fmt: AudioFormat, mixerInfo: Info): Option[DataLine] = {
    Some(AudioSystem.getTargetDataLine(fmt, mixerInfo))
  }
}

private class OutputSoundInterface(val name: String) extends SoundInterface {
  override def getAudioSystemLine(fmt: AudioFormat, mixerInfo: Info): Option[DataLine] = {
    Some(AudioSystem.getSourceDataLine(fmt, mixerInfo))
  }
}

object SoundInterfaceFactory {
  def apply(interfaceType: SoundInterfaceType, interfaceName: String): SoundInterface = interfaceType match {
    case SoundInterfaceType.OUTPUT => new OutputSoundInterface(interfaceName)
    case SoundInterfaceType.INPUT => new InputSoundInterface(interfaceName)
  }
}