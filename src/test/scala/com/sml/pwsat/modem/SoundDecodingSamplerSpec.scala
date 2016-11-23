package com.sml.pwsat.modem

import java.io.File
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

import scala.io.Source

class SoundDecodingSamplerSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "A SoundDecodingSampler" should "process an Audio File into samples" in {
    samplesFromAudioFile("/packet12.wav") should equal (samplesFromReferenceFile("/packet12.samples"))
  }

  def samplesFromReferenceFile(path: String): List[Double] = Source.fromFile(file(path)).getLines.map(x => x.toDouble).toList

  def samplesFromAudioFile(audioFilePath: String): List[Double] = {
    val queue: BlockingQueue[Double] = new LinkedBlockingQueue[Double]
    new FileDecodingSampleProducer(file(audioFilePath), queue).run()
    import scala.collection.JavaConverters._
    queue.asScala.toList
  }

  def file(path: String): File = new File(this.getClass.getResource(path).getPath)

}