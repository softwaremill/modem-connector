package com.softwaremill.agwpe

import java.io.DataOutputStream

object Sender {

  val IdSize = 10
  val Byte = 8
  val HalfByte = 4

  def sendAGWPEFrame(socketOut: DataOutputStream, port: Int, dataKind: Char, pid: Int, callFrom: String, callTo: String, data: Array[Byte]): Unit = {
    frameBytes(port, dataKind, pid, callFrom, callTo, data).foreach(socketOut.write)
  }

  private def frameBytes(port: Int, dataKind: Char, pid: Int, callFrom: String, callTo: String, data: Array[Byte]): List[Int] = {
    def callBytes(string: String): List[Int] = (string.toList.map(_.toInt) ++ List.fill(IdSize - string.length)(0)).take(IdSize)
    def dataBytes(dataOpt: Option[Array[Byte]]): List[Int] = dataOpt match {
      case None => List.fill[Int](Byte)(0)
      case Some(someData) => List.tabulate[Int](HalfByte)(pos => someData.length >>> (Byte*pos)) ++ List.fill[Int](HalfByte)(0) ++ someData.map(_.toInt)
    }
    val headerBytes = port :: 0 :: 0 :: 0 :: dataKind.toInt :: 0 :: pid :: 0 :: Nil
    headerBytes ++ callBytes(callFrom) ++ callBytes(callTo) ++ dataBytes(Option(data))
  }

}