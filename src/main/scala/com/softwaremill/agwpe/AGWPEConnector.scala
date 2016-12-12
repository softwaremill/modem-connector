package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream}
import java.net.{InetSocketAddress, Socket}
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.ax25.AX25Frame
import com.softwaremill.service.{AGWPEFrameConsumer, Observer}
import com.typesafe.scalalogging.LazyLogging


class AGWPEConnector(val host: String = AGWPESettings.host, val port: Int = AGWPESettings.port, val timeout: Int = AGWPESettings.timeout) extends LazyLogging {

  val MaxInactivityMSecs: Long = 30000L
  var lastFrameRcvTime: Long = 0

  val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
  val sockToAGWPE: Socket = createSocket()

  val socketIn: DataInputStream = new DataInputStream(sockToAGWPE.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(sockToAGWPE.getOutputStream)
  val agwpeProducer: AGWPEFrameProducer = new AGWPEFrameProducer(socketIn, socketOut, queue)
  val agwpeConsumer: AGWPEFrameConsumer = new AGWPEFrameConsumer(queue)

  val timer = new java.util.Timer()

  val inactivityMonitor = new java.util.TimerTask {
    def run() = {
      val now: Long = System.currentTimeMillis()
      val delta: Long = now - lastFrameRcvTime
      if (delta >= MaxInactivityMSecs) {
        try {
          sendVersionCommand()
        } catch {
          case e: Exception => logger.error("Sending version command to keep connection alive failed.")
        }
      }
    }
  }

  private def createSocket(): Socket = {
    val sockToAGWPE: Socket = new Socket
    sockToAGWPE.setSoLinger(false, 1)
    sockToAGWPE.connect(new InetSocketAddress(host, port), timeout)
    sockToAGWPE
  }

  def startConnection(): Unit = {
    lastFrameRcvTime = System.currentTimeMillis()
    timer.scheduleAtFixedRate(inactivityMonitor, MaxInactivityMSecs, MaxInactivityMSecs)
    val consumerThread: Thread = new Thread(agwpeConsumer)
    val producerThread: Thread = new Thread(agwpeProducer)
    consumerThread.start()
    producerThread.start()
  }

  def addAX25MessageObserver(observer: Observer[AX25Frame]): Unit = {
    agwpeConsumer.addObserver(observer)
  }

  def addServiceMessageObserver(observer: Observer[ServiceMessage]): Unit = {
    agwpeProducer.addObserver(observer)
  }

  def sendAx25Frame(frame: AX25Frame): Unit = {
    agwpeProducer.sendAx25Frame(frame)
  }

  private def sendVersionCommand(): Unit = {
    agwpeProducer.send(AGWPEFrame.versionFrame)
    lastFrameRcvTime = System.currentTimeMillis
  }
}
