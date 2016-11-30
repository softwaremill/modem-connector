package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream, IOException}
import java.net.{Socket, SocketException}


class AGWPEListener(socket: Socket, host: String, port: Int) extends Runnable {

  val socketIn: DataInputStream = new DataInputStream(socket.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(socket.getOutputStream)

  override def run(): Unit = {
    while (true) {
      try {
        val frame: AGWPEFrame = receiveAGWPEFrame(socketIn)
        val result: Char = (frame.dataKind & 0xFFFF).toChar
        println("RESULT: " + result)
      } catch {
        case se: SocketException => se.printStackTrace()
        case ioe: IOException => ioe.printStackTrace()
        case all: Throwable => all.printStackTrace()
      }
    }
  }

  def receiveAGWPEFrame(is: DataInputStream): AGWPEFrame = {
    val port: Int = is.readUnsignedByte() | (is.readUnsignedByte() << 8) | (is.readUnsignedByte() << 16) | (is.readUnsignedByte() << 24)
    val datakind: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort
    val pid: Short = (is.readUnsignedByte() | (is.readUnsignedByte() << 8)).toShort

        def findCallValue(): Option[String] = {
//          import scala.util.control.Breaks._
          val ch: Int = is.readUnsignedByte()
          is.skipBytes(9)
          None
//          if (0 == ch) {
//            is.skipBytes(9)
//            None
//          } else {
//            val b: StringBuilder = new StringBuilder(9)
//            b.append(ch.toChar)
//            breakable {
//              (1 to 9).foreach(i => {
//                if (0 == ch) {
//                  break
//                }
//                b.append(ch.toChar)
//              })
//            }
//            if (b.length < 9) {
//              is.skipBytes(9 - b.length)
//            }
//            Some(b.toString)
//          }
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

  def sendAGWPEFrame(port: Int, dataKind: Char, pid: Int, callFrom: String, callTo: String, data: Array[Byte]): Unit = {
    socketOut.write(port)
    socketOut.write(0)
    socketOut.write(0)
    socketOut.write(0)
    socketOut.write(dataKind)
    socketOut.write(0)
    socketOut.write(pid)
    socketOut.write(0)
    if (null == callFrom) {
      for (elem <- (0 to 10)) {
        socketOut.write(0)
      }
    } else {
      val max: Int = Math.min(callFrom.length(), 10)
      for (elem <- (0 to max)) {
        socketOut.write(callFrom.charAt(elem))
      }
      for (elem <- (0 to 10)) {
        socketOut.write(0)
      }
    }
    if (null == callTo) {
      for (elem <- (0 to 10)) {
        socketOut.write(0)
      }
    } else {
      val max: Int = Math.min(callFrom.length(), 10)
      for (elem <- (0 to max)) {
        socketOut.write(callFrom.charAt(elem))
      }
      for (elem <- (0 to 10)) {
        socketOut.write(0)
      }
    }
    if (null == data) {
      socketOut.write(0) // dataLen
      socketOut.write(0)
      socketOut.write(0)
      socketOut.write(0)
      socketOut.write(0) // user
      socketOut.write(0)
      socketOut.write(0)
      socketOut.write(0)
    } else {
      val v: Int = data.length
      socketOut.write(v); // dataLen
      socketOut.write(v >>> 8)
      socketOut.write(v >>> 16)
      socketOut.write(v >>> 24)
      socketOut.write(0) // user
      socketOut.write(0)
      socketOut.write(0)
      socketOut.write(0)
      socketOut.write(data)
    }
    socketOut.flush()
  }
}
