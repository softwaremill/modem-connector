package com.softwaremill.modemconnector

trait Descrambler {
  def descramble(data: Array[Byte]): Array[Byte]
}
