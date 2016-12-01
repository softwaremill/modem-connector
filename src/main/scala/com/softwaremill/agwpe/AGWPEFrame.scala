package com.softwaremill.agwpe

case class AGWPEFrame(port: Int, dataKind: Short, pid: Short,
                      callFrom: Option[String], callTo: Option[String],
                      dataLength: Int, user: Int, data: Array[Byte]) {}