package com.softwaremill.service

import java.util.concurrent.BlockingQueue

import com.typesafe.scalalogging.LazyLogging

abstract class Consumer[T](queue: BlockingQueue[T]) extends Runnable with LazyLogging {

  override def run(): Unit = {
    logger.info("Starting Consumer....")
    try {
      while (!Thread.currentThread.isInterrupted) {
        val sample = queue.take()
        consume(sample)
      }
    } catch {
      case ie: InterruptedException => logger.info("Consumer stopped with interrupt")
    }
  }

  def consume(sample: T): Unit

}