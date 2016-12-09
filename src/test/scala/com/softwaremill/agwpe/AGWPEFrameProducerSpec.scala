package com.softwaremill.agwpe

import java.io._
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.FrameUtils
import com.softwaremill.ax25.AX25Frame
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameProducerSpec extends FlatSpec with Matchers with BeforeAndAfter {

  val Port: Int = 0
  val Pid: Short = 0

  "An AX25 Frame" should "be sent out to SoundModem" in {
    //given
    val ax25frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream
    val producer: AGWPEFrameProducer = agwpeFrameProducer(baos)
    //when
    producer.sendAx25Frame(ax25frame)
    val agwpe: AGWPEFrame = agwpeFrame(baos.toByteArray)
    //then
    agwpe.command shouldEqual 'K'
    toString(agwpe.data.get) should include("Op. Andrej")
  }

  "An AGWPEFrame" should "be added to the producer queue when 'K' received" in {
    //given
    val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
    val producer: AGWPEFrameProducer = agwpeFrameProducer(queue)
    val agwpe: AGWPEFrame = AGWPEFrame(Port, 'K', Pid)
    //when
    producer.receiveCommand(agwpe)
    //then
    queue.size() shouldBe 1
  }

  private def agwpeFrame(data: Array[Byte]): AGWPEFrame = AGWPEFrame(new DataInputStream(new ByteArrayInputStream(data)))

  private def agwpeFrameProducer(baos: ByteArrayOutputStream): AGWPEFrameProducer = {
    val dataInputStream: DataInputStream = new DataInputStream(new ByteArrayInputStream(Array()))
    val dataOutputStream: DataOutputStream = new DataOutputStream(baos)
    val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
    val agwpeFrame: AGWPEFrameProducer = new AGWPEFrameProducer(dataInputStream, dataOutputStream, queue)
    baos.reset()
    agwpeFrame
  }

  private def agwpeFrameProducer(queue: BlockingQueue[AGWPEFrame]): AGWPEFrameProducer = {
    val dataInputStream: DataInputStream = new DataInputStream(new ByteArrayInputStream(Array()))
    val dataOutputStream: DataOutputStream = new DataOutputStream(new ByteArrayOutputStream())
    new AGWPEFrameProducer(dataInputStream, dataOutputStream, queue)
  }

  private def toString(data: Array[Byte]): String = new String(data)

}