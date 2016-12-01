package com.softwaremill.agwpe

import java.io.DataInputStream

object Receiver {


  def receiveAGWPEFrame(is: DataInputStream): AGWPEFrame = {
    val port: Int = is.readUnsignedByte() | (is.readUnsignedByte() << 8) | (is.readUnsignedByte() << 16) | (is.readUnsignedByte() << 24)
    val datakind: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort
    val pid: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort

    def findCallValue(): Option[String] = {

      import scala.util.control.Breaks._

      val ch: Int = is.readUnsignedByte()
      if (0 == ch) {
        is.skipBytes(9)
        None
      } else {
        val b: StringBuilder = new StringBuilder(9)
        b.append(ch.toChar)
        breakable {
          (1 to 9).foreach(i => {
            val ch: Int = is.readUnsignedByte()
            if (0 == ch) {
              break
            }
            b.append(ch.toChar)
          })
        }
        if (b.length < 9) {
          is.skipBytes(9 - b.length)
        }
        Some(b.toString)
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
