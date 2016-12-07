package com.softwaremill.ax25

class AX25Callsign(val callsign: String, val h_c: Boolean, val reserved: Byte, val ssid: Byte, val last: Boolean){
}

object AX25Callsign {

  val CallsignSize: Int = 6

  def apply(data: Array[Byte]): AX25Callsign = {
    def getChar(charByte: Byte): Char = ((charByte & 0xFF) >> 1).toChar
    val callsign: String = new String(data.slice(0, CallsignSize).map(getChar), 0, CallsignSize).trim
    val flag = data(CallsignSize)
    val h_c: Boolean = (flag & 0x80) != 0
    val reserved: Byte = ((flag & 0x60) >> 5).toByte
    val ssid: Byte = ((flag & 0x1E) >> 1).toByte
    val last: Boolean = (flag & 0x01) != 0
    new AX25Callsign(callsign, h_c, reserved, ssid, last)
  }
}