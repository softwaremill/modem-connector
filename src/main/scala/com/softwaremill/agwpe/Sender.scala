package com.softwaremill.agwpe

import java.io.DataOutputStream

object Sender {

  def sendAGWPEFrame(socketOut: DataOutputStream, port: Int, dataKind: Char, pid: Int, callFrom: String, callTo: String, data: Array[Byte]): Unit = {
        socketOut.write(port)
        socketOut.write(0)
        socketOut.write(0)
        socketOut.write(0)
        socketOut.write(dataKind)
        socketOut.write(0)
        socketOut.write(pid)
        socketOut.write(0)
        if (null == callFrom) {
          (0 to 9).foreach(x => socketOut.write(0))
        } else {
//            var i:Int = 0
//            for (i = 0; i < Math.min(callFrom.length(), 10); i++) {
//                socketOut.write(callFrom.charAt(i));
//            }
//            for (; i < 10; i++) {
//                socketOut.write(0);
//            }
        }
        if (null == callTo) {
          (0 to 9).foreach(x => socketOut.write(0))
        } else {
//            var i:Int = 0;
//            for (i = 0; i < Math.min(callTo.length(), 10); i++) {
//                socketOut.write(callTo.charAt(i))
//            }
//            for (; i < 10; i++) {
//                socketOut.write(0)
//            }
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
            val v:Int = data.length
            socketOut.write(v) // dataLen
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
