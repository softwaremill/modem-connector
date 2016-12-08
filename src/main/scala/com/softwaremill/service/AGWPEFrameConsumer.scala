package com.softwaremill.service

import java.util.concurrent.BlockingQueue

import com.softwaremill.agwpe.AGWPEFrame
import com.softwaremill.ax25.AX25Frame

class AGWPEFrameConsumer(queue: BlockingQueue[AGWPEFrame]) extends Consumer(queue) with Subject[AX25Frame] {

  override def consume(agwpe: AGWPEFrame): Unit = {
    val ax25frame: AX25Frame = prepareAX25Frame(agwpe)
    notifyObservers(ax25frame)
  }

  private def prepareAX25Frame(agwpe: AGWPEFrame): AX25Frame = {
    AX25Frame(agwpe.data.get)
  }
}
