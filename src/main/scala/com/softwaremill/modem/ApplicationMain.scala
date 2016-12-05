package com.softwaremill.modem

import com.softwaremill.agwpe.AGWPEConnector

object ApplicationMain extends App {
  val connector: AGWPEConnector = new AGWPEConnector
  connector.open()
}

