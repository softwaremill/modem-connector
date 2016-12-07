package com.softwaremill

import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}

import com.softwaremill.agwpe.{AGWPEConnector, AGWPEFrame}

object ApplicationMain extends App {
  val queue: BlockingQueue[AGWPEFrame] = new LinkedBlockingQueue[AGWPEFrame]
  val connector: AGWPEConnector = new AGWPEConnector(queue)
  connector.open()
}

