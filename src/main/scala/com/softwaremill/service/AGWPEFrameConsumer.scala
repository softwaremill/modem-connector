package com.softwaremill.service

import java.util.concurrent.BlockingQueue

import com.softwaremill.agwpe.AGWPEFrame

//todo: change subject type to AX25 Frame
class AGWPEFrameConsumer(queue: BlockingQueue[AGWPEFrame]) extends Consumer(queue) with Subject[String] {


  override def consume(agwpe: AGWPEFrame): Unit = {
    val ax25frame: String = prepareAX25Frame(agwpe)
    notifyObservers(ax25frame)
  }

  def prepareAX25Frame(agwpe: AGWPEFrame): String = ???
}
