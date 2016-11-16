package com.sml.pwsat.modem

import java.io.File
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
    AudioSystem.getMixerInfo.toSeq.find(_.getName.equalsIgnoreCase(name)).map(mi => getAudioLine(fmt, mi).get)
  }

  protected def getAudioLine(fmt: AudioFormat, mixerInfo: Mixer.Info): Option[DataLine]

  override def toString = "System Interface Name: " + name
}

private class InputSoundInterface(val name: String) extends SoundInterface {
  override def getAudioLine(fmt: AudioFormat, mixerInfo: Info): Option[DataLine] = {
    Some(AudioSystem.getTargetDataLine(fmt, mixerInfo))
  }
}

private class OutputSoundInterface(val name: String) extends SoundInterface {
  override def getAudioLine(fmt: AudioFormat, mixerInfo: Info): Option[DataLine] = {
    Some(AudioSystem.getSourceDataLine(fmt, mixerInfo))
  }
}

private class FileInputSoundInterface(val path: String) extends SoundInterface {
  override def name: String = "File Interface on: " + path

  override def getAudioLine(fmt: AudioFormat, mixerInfo: Info): Option[DataLine] = {
    None
  }

  override def getDataLine: Option[DataLine] = {
    val soundFile: File = new File(path)
    val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(soundFile)
    val audioFormat: AudioFormat = audioStream.getFormat
    val dataLine: SourceDataLine = AudioSystem.getSourceDataLine(audioFormat)
    Some(dataLine)
  }
}

object SoundInterfaceFactory {

  def apply(interfaceType: SoundInterfaceType, uri: String): SoundInterface = interfaceType match {
    case SoundInterfaceType.OUTPUT => new OutputSoundInterface(uri)
    case SoundInterfaceType.INPUT => new InputSoundInterface(uri)
    case SoundInterfaceType.FILE_INPUT => new FileInputSoundInterface(uri)
  }

}