package com.softwaremill.service

import java.util.concurrent.BlockingQueue

abstract class Consumer[T](queue: BlockingQueue[T]) {
  def consume(sample: T): Unit
}
