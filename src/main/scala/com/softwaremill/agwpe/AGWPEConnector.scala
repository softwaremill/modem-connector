package com.softwaremill.agwpe

import java.net.{InetSocketAddress, Socket}
import java.util.concurrent.BlockingQueue

import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.{AGWPEFrameConsumer, FrameObserver}


class AGWPEConnector(queue: BlockingQueue[AGWPEFrame]) {

  def startConnection(ax25FrameObservers: Seq[FrameObserver[AX25Frame]]): Unit = {
    val sockToAGWPE: Socket = new Socket
    sockToAGWPE.setSoLinger(false, 1)
    sockToAGWPE.connect(new InetSocketAddress(AGWPESettings.host, AGWPESettings.port), AGWPESettings.timeout)
    val agwpeProducer: AGWPEFrameProducer = new AGWPEFrameProducer(sockToAGWPE, queue)
    val agwpeConsumer: AGWPEFrameConsumer = new AGWPEFrameConsumer(queue)

    ax25FrameObservers.foreach(agwpeConsumer.addObserver)

    val t1: Thread = new Thread(agwpeConsumer)
    val t2: Thread = new Thread(agwpeProducer)
    t1.start()
    t2.start()
  }
}
