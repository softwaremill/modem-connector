package com.softwaremill.agwpe

import java.net.{InetSocketAddress, Socket}

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Framing, Source, Tcp}
import akka.util.ByteString


class AGWPEConnector {

  def open(): Unit = {
    val sockToAGWPE: Socket = new Socket
    sockToAGWPE.setSoLinger(false, 1)
    sockToAGWPE.connect(new InetSocketAddress(AGWPESettings.host, AGWPESettings.port), AGWPESettings.timeout)
    val listener2: AGWPEHandler = new AGWPEHandler(sockToAGWPE)

    val t2: Thread = new Thread(listener2, "AGWPEConnector[" + AGWPESettings.host + ':' + AGWPESettings.port + ']')
    t2.setDaemon(false)
    t2.start()
  }
}
