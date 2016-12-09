package com.softwaremill.agwpe

import java.io._
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.FrameUtils
import com.softwaremill.ax25.AX25Frame
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameProducerSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AX25 Frame" should "be sent out to SoundModem" in {
    //given
    val ax25frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    val dataInputStream: DataInputStream = new DataInputStream(new ByteArrayInputStream(Array()))
    val baos: ByteArrayOutputStream = new ByteArrayOutputStream
    val dataOutputStream: DataOutputStream = new DataOutputStream(baos)
    val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
    val producer: AGWPEFrameProducer = new AGWPEFrameProducer(dataInputStream, dataOutputStream, queue)
    //when
    baos.reset()
    producer.sendAx25Frame(ax25frame)
    val result: Array[Byte] = baos.toByteArray
    val agwpe: AGWPEFrame = AGWPEFrame(new DataInputStream(new ByteArrayInputStream(result)))
    val data = new String(agwpe.data.get)
    //then
    agwpe.command shouldEqual 'K'
    data should include("Op. Andrej")
  }
}