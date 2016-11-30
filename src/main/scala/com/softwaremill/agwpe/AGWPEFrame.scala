package com.softwaremill.agwpe

class AGWPEFrame(val port: Int, val dataKind: Short, val pid: Short,
                 val callFrom: Option[String], val callTo: Option[String],
                 val dataLength: Int, val user: Int, val data: Array[Byte]) {

}