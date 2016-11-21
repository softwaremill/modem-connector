package com.sml.pwsat.modem

import java.io.File
import java.util.concurrent.BlockingQueue
import javax.sound.sampled._

class FileDecodingSampleProducer(soundFile: File, queue: BlockingQueue[Double]) extends SampleProducer(queue) {

  val BufferSize: Int = 128000

  override def run(): Unit = {
    val audioStream: AudioInputStream = AudioSystem.getAudioInputStream(soundFile)
    val audioFormat: AudioFormat = audioStream.getFormat
    logger.info("Audio Format: " + audioFormat)
    val buf: Array[Byte] = new Array[Byte](BufferSize)
    val samples: Int = BufferSize / audioFormat.getFrameSize
    val fltbuf: Array[Double] = new Array[Double](samples)

    var nBytesRead: Int = 0
    while (nBytesRead != -1) {
      try {
        nBytesRead = audioStream.read(buf, 0, BufferSize)
        var offset: Int = 0
        var index: Int = 0
        while (offset < BufferSize) {
          offset = produceSample(audioFormat, buf, offset, fltbuf, index)
          index += 1
        }
        process(fltbuf)
      } catch {
        case e: Throwable => e.printStackTrace()
      }
    }
  }
}