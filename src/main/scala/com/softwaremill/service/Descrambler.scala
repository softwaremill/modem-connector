package com.softwaremill.service


trait Descrambler {
  def descramble(data: Array[Byte]): Array[Byte]
}
