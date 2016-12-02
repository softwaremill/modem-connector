package com.softwaremill.agwpe

import java.io.DataOutputStream

object Sender {
  def send(socketOut: DataOutputStream, agwpeFrame: AGWPEFrame): Unit = {
    agwpeFrame.bytes.foreach(socketOut.write)
  }
}