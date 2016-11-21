package com.sml.pwsat.modem

import java.util.concurrent.BlockingQueue

abstract class SampleConsumer[T](queue: BlockingQueue[T]) extends Runnable {

  override def run(): Unit = {
    while (true) {
      val sample = queue.take()
      consume(sample)
    }
  }

  def consume(sample: T): Unit
}

