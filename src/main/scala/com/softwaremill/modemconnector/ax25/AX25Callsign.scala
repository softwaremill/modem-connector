package com.softwaremill.modemconnector.ax25

class AX25Callsign(val callsign: String, val h_c: Boolean, val reserved: Byte, val ssid: Byte, val last: Boolean) {

  def toBytes: Array[Byte] = {
    // scalastyle:off magic.number
    val callsignBytes: Array[Byte] = callsign.map(ch => ((ch.toInt & 0x7F) << 1).toByte).toArray
    val padding: Array[Byte] = Array.fill(AX25Callsign.CallsignSize - callsign.length)(0x40.toByte)
    val flag: Byte = ((ssid << 1) | ((reserved & 3) << 5) | (if (h_c) 0x80 else 0) | (if (last) 1 else 0)).toByte
    // scalastyle:on magic.number
    (callsignBytes ++ padding).take(AX25Callsign.CallsignSize) ++ Array(flag)
  }

  override def toString: String = if (ssid == 0) callsign else callsign + '-' + ssid.toInt

}

object AX25Callsign {

  val CallsignSize: Int = 6

  def apply(data: Array[Byte]): AX25Callsign = {
    def toChar(charByte: Byte): Char = ((charByte & 0xFF) >> 1).toChar

    val callsign: String = new String(data.slice(0, CallsignSize).map(toChar), 0, CallsignSize).trim
    val flag = data(CallsignSize)
    val h_c: Boolean = (flag & 0x80) != 0
    val reserved: Byte = ((flag & 0x60) >> 5).toByte
    val ssid: Byte = ((flag & 0x1E) >> 1).toByte
    val last: Boolean = (flag & 0x01) != 0
    new AX25Callsign(callsign, h_c, reserved, ssid, last)
  }
}