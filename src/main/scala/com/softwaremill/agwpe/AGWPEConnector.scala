package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream}
import java.net.{InetSocketAddress, Socket}
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.{AGWPEFrameConsumer, FrameObserver}


class AGWPEConnector(val host: String = AGWPESettings.host, val port: Int = AGWPESettings.port, val timeout: Int = AGWPESettings.timeout) {

  val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
  val sockToAGWPE: Socket = new Socket

  sockToAGWPE.setSoLinger(false, 1)
  sockToAGWPE.connect(new InetSocketAddress(host, port), timeout)

  val socketIn: DataInputStream = new DataInputStream(sockToAGWPE.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(sockToAGWPE.getOutputStream)
  val agwpeProducer: AGWPEFrameProducer = new AGWPEFrameProducer(socketIn, socketOut, queue)
  val agwpeConsumer: AGWPEFrameConsumer = new AGWPEFrameConsumer(queue)


  def startConnection(ax25FrameObservers: Seq[FrameObserver[AX25Frame]]): Unit = {
    ax25FrameObservers.foreach(agwpeConsumer.addObserver)
    val t1: Thread = new Thread(agwpeConsumer)
    val t2: Thread = new Thread(agwpeProducer)
    t1.start()
    t2.start()
  }

  def sendAx25Frame(frame: AX25Frame): Unit = {
    agwpeProducer.sendAx25Frame(frame)
  }
}
