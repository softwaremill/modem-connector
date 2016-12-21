package com.softwaremill.modemconnector.agwpe

import java.util.concurrent.BlockingQueue

import com.softwaremill.modemconnector.ax25.AX25Frame
import com.softwaremill.modemconnector.{Consumer, Descrambler, Subject}

class AGWPEFrameConsumer(queue: BlockingQueue[AGWPEFrame], descrambler: Option[Descrambler] = None) extends Consumer(queue) with Subject[AX25Frame] {

  override def consume(agwpe: AGWPEFrame): Unit = {
    val ax25frame: AX25Frame = prepareAX25Frame(agwpe)
    notifyObservers(ax25frame)
  }

  private def prepareAX25Frame(agwpe: AGWPEFrame): AX25Frame = {
    descrambler match {
      case Some(ds) => AX25Frame(ds.descramble(agwpe.data.get))
      case None => AX25Frame(agwpe.data.get)
    }
  }
}