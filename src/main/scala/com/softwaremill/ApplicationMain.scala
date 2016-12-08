package com.softwaremill

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.agwpe.{AGWPEConnector, AGWPEFrame}
import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.FrameObserver

object ApplicationMain extends App {
  val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
  val connector: AGWPEConnector = new AGWPEConnector(queue)
  val observer: PrintLineAX25FrameObserver = new PrintLineAX25FrameObserver
  connector.startConnection(List(observer))
}

class PrintLineAX25FrameObserver extends FrameObserver[AX25Frame] {
  override def receiveUpdate(subject: AX25Frame): Unit = {
    println(subject)
  }
}