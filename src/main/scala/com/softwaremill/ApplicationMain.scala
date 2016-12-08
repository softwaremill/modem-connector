package com.softwaremill

import com.softwaremill.agwpe.AGWPEConnector
import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.FrameObserver

object ApplicationMain extends App {
  val connector: AGWPEConnector = new AGWPEConnector
  val observer: PrintLineAX25FrameObserver = new PrintLineAX25FrameObserver
  connector.startConnection(List(observer))
}

class PrintLineAX25FrameObserver extends FrameObserver[AX25Frame] {
  override def receiveUpdate(subject: AX25Frame): Unit = {
    println(subject)
  }
}