package com.softwaremill.modemconnector

import com.softwaremill.modemconnector.agwpe.AGWPEConnector
import com.softwaremill.modemconnector.ax25.AX25Frame

object ApplicationMain extends App {
  val connector: AGWPEConnector = new AGWPEConnector()
  val observer: PrintLineAX25Observer = new PrintLineAX25Observer
  connector.addAX25MessageObserver(observer)
  connector.startConnection()
}

class PrintLineAX25Observer extends Observer[AX25Frame] {
  override def receiveUpdate(subject: AX25Frame): Unit = {
    // scalastyle:off regex
    println(subject)
    // scalastyle:on regex
  }
}