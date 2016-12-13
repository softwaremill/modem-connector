package com.softwaremill

import com.softwaremill.agwpe.AGWPEConnector
import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.Observer

object ApplicationMain extends App {
  val connector: AGWPEConnector = new AGWPEConnector
  val observer: PrintLineAX25Observer = new PrintLineAX25Observer
  connector.startConnection()
}

class PrintLineAX25Observer extends Observer[AX25Frame] {
  override def receiveUpdate(subject: AX25Frame): Unit = {
    // scalastyle:off regex
    println(subject)
    // scalastyle:on regex
  }
}