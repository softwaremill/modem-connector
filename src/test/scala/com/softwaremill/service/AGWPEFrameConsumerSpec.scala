package com.softwaremill.service

import java.io.DataInputStream
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.FrameUtils
import com.softwaremill.agwpe.AGWPEFrame
import com.softwaremill.ax25.AX25Frame
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameConsumerSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AGWPEFrameConsumer" should "process given element in the queue" in {
    //given
    val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
    val consumer: AGWPEFrameConsumer = new AGWPEFrameConsumer(queue)
    val observer: TestObserver = new TestObserver
    consumer.addObserver(observer)
    val dis: DataInputStream = FrameUtils.dataStream("/dataFrame.bin")
    val frame: AGWPEFrame = AGWPEFrame(dis)
    //when
    consumer.consume(frame)
    //then
    observer.observerCalled shouldBe true
  }
}

class TestObserver extends FrameObserver[AX25Frame] {
  var observerCalled: Boolean = false

  override def receiveUpdate(subject: AX25Frame): Unit = {
    this.observerCalled = true
  }
}