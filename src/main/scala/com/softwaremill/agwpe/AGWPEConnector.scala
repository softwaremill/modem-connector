package com.softwaremill.agwpe

import java.net.{InetSocketAddress, Socket}
import java.util.concurrent.BlockingQueue


class AGWPEConnector(queue: BlockingQueue[AGWPEFrame]) {

  def open(): Unit = {
    val sockToAGWPE: Socket = new Socket
    sockToAGWPE.setSoLinger(false, 1)
    sockToAGWPE.connect(new InetSocketAddress(AGWPESettings.host, AGWPESettings.port), AGWPESettings.timeout)
    val listener2: AGWPEFrameProducer = new AGWPEFrameProducer(sockToAGWPE, queue)

    val t2: Thread = new Thread(listener2, "AGWPEConnector[" + AGWPESettings.host + ':' + AGWPESettings.port + ']')
    t2.setDaemon(false)
    t2.start()
  }
}
