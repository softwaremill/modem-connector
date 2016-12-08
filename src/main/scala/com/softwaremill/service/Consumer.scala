package com.softwaremill.service

import java.util.concurrent.BlockingQueue

import com.typesafe.scalalogging.LazyLogging

abstract class Consumer[T](queue: BlockingQueue[T]) extends Runnable with LazyLogging {

  override def run(): Unit = {
    logger.info("Starting Consumer....")
    while (true) {
      val sample = queue.take()
      consume(sample)
    }
  }

  def consume(sample: T): Unit

}