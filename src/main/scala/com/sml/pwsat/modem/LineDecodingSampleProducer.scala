package com.sml.pwsat.modem

import java.util.concurrent.BlockingQueue
import javax.sound.sampled._

class LineDecodingSampleProducer(val targetDataLine: TargetDataLine,
                                 val audioFormat: AudioFormat, queue: BlockingQueue[Double]) extends SampleProducer(queue) {

  val BufferSize: Int = 8192

  override def run(): Unit = {
    try {
      targetDataLine.open(audioFormat, BufferSize)
      targetDataLine.start()
    } catch {
      case e: Exception => logger.error("Cannot start sampler.", e)
    }

    val buf: Array[Byte] = new Array[Byte](BufferSize)
    val samples: Int = BufferSize / audioFormat.getFrameSize
    val fltbuf: Array[Double] = new Array[Double](samples)

    while (true) {
      targetDataLine.read(buf, 0, BufferSize)
      var offset: Int = 0
      var index: Int = 0
      while (offset < BufferSize) {
        offset = produceSample(audioFormat, buf, offset, fltbuf, index)
        index += 1
      }
      process(fltbuf)
    }
    targetDataLine.stop()
    targetDataLine.flush()
    targetDataLine.close()
  }
}
