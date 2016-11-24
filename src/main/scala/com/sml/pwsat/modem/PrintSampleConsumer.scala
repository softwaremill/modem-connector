package com.sml.pwsat.modem

import java.util.concurrent.BlockingQueue

class PrintSampleConsumer(queue: BlockingQueue[Double]) extends SampleConsumer(queue) {

  override def consume(sample: Double): Unit = {
    println("%.9f" format sample)
  }
}
