package com.sml.pwsat.modem.demodulator

import java.io.File
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.sml.pwsat.modem.FileDecodingSampleProducer
import com.sml.pwsat.modem.packet.{AX25Packet, PacketHandler}
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AFSK1200DemodulatorSpec extends FlatSpec with Matchers with BeforeAndAfter {

  val SampleRate: Int = 11025
  val FilterLength: Int = 1
  val Emphasis: Int = 0

  val handler: TestPacketHandler = new TestPacketHandler
  val demodulator: AFSK1200Demodulator = new AFSK1200Demodulator(SampleRate, FilterLength, Emphasis, handler)

  "A AFSK1200DemodulatorSpec" should "demodulate given audio signal into AX25 frames" in {
    samplesFromAudioFile("/packet12.wav").foreach(packet => demodulator.addSample(packet.toFloat))
    handler.getResult() shouldBe "[NOCALL>CQ:hello\\x0d]"
  }

  def samplesFromAudioFile(audioFilePath: String): List[Double] = {
    val queue: BlockingQueue[Double] = new LinkedBlockingQueue[Double]
    new FileDecodingSampleProducer(file(audioFilePath), queue).run()
    import scala.collection.JavaConverters._
    queue.asScala.toList
  }

  def file(path: String): File = new File(this.getClass.getResource(path).getPath)

}

class TestPacketHandler extends PacketHandler {
  var result = ""

  override def handlePacket(bytes: Array[Byte]): Unit = {
    result = AX25Packet.format(bytes)
  }

  def getResult(): String = {
    result
  }
}
