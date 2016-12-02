package com.softwaremill.agwpe

class AGWPEFrame(val port: Int, val dataKind: Short, val pid: Short,
                 val callFrom: Option[String], val callTo: Option[String],
                 val dataLength: Int, val user: Int, val data: Option[Array[Byte]]) {

  val CallSize = 10
  val Byte = 8
  val HalfByte = 4

  def bytes: List[Int] = {
    def callBytes(call: String): List[Int] = (call.toList.map(_.toInt) ++ List.fill(CallSize - call.length)(0)).take(CallSize)

    def dataBytes(dataOpt: Option[Array[Byte]]): List[Int] = dataOpt match {
      case None => List.fill(Byte)(0)
      case Some(someData) => List.tabulate(HalfByte)(pos => someData.length >>> (Byte * pos)) ++ List.fill(HalfByte)(0) ++ someData.map(_.toInt)
    }

    val headerBytes: List[Int] = port :: 0 :: 0 :: 0 :: dataKind.toInt :: 0 :: pid.toInt :: 0 :: Nil
    headerBytes ++ callBytes(callFrom.getOrElse("")) ++ callBytes(callTo.getOrElse("")) ++ dataBytes(data)
  }

}

object AGWPEFrame {

  def apply(port: Int, dataKind: Short, pid: Short,
            callFrom: Option[String], callTo: Option[String],
            dataLength: Int, user: Int, data: Option[Array[Byte]]): AGWPEFrame = {
    new AGWPEFrame(port, dataKind, pid, callFrom, callTo, dataLength, user, data)
  }

  def apply(port: Int, dataKind: Short, pid: Short): AGWPEFrame = {
    new AGWPEFrame(port, dataKind, pid, None, None, 0, 0, None)
  }

  def apply(dataKind: Short): AGWPEFrame = {
    new AGWPEFrame(0, dataKind, 0, None, None, 0, 0, None)
  }

  def version: AGWPEFrame = apply('R')

  def info: AGWPEFrame = apply('G')

  def monitorOn: AGWPEFrame = apply('k')

}