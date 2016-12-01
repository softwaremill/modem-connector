package com.softwaremill.agwpe

import java.io.DataInputStream

object AGWPEFrameHandler {

  def handleAGWPEFrame(is: DataInputStream): AGWPEFrame = {
    val port: Int = is.readUnsignedByte() | (is.readUnsignedByte() << 8) | (is.readUnsignedByte() << 16) | (is.readUnsignedByte() << 24)
    val datakind: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort
    val pid: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort

    def findCallValue(): Option[String] = {
      val CallValueSize: Int = 9
      val ch: Int = is.readUnsignedByte()
      if (0 == ch) {
        is.skipBytes(CallValueSize)
        None
      } else {
        val result = List.tabulate(CallValueSize)(c => {
          val ch: Int = is.readUnsignedByte()
          if (0 == ch) {
            None
          }
          else {
            Some(ch.toChar)
          }
        }).flatMap(b => b.toString).mkString("")
        Some(result)
      }
    }
    val callFrom: Option[String] = findCallValue()
    val callTo: Option[String] = findCallValue()
    val dataLength: Int = is.readUnsignedByte() | (is.readUnsignedByte() << 8) | (is.readUnsignedByte() << 16) | (is.readUnsignedByte() << 24)
    val user: Int = is.readUnsignedByte() | (is.readUnsignedByte() << 8) | (is.readUnsignedByte() << 16) | (is.readUnsignedByte() << 24)

    def readData(): Array[Byte] = {
      var count = 0
      val data: Array[Byte] = new Array[Byte](dataLength)
      while (count < dataLength) {
        count = is.read(data, count, dataLength - count)
      }
      data
    }

    val data: Array[Byte] = readData()

    new AGWPEFrame(port, datakind, pid, callFrom, callTo, dataLength, user, data)
  }
}
