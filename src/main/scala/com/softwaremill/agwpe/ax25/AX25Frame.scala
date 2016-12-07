package com.softwaremill.agwpe.ax25

import scala.collection.mutable.ListBuffer

class AX25Frame(val sender: AX25Callsign, val dest: AX25Callsign,
                val digipeaters: Array[AX25Callsign],
                val ctl: Byte,
                val pid: Option[Byte],
                val body: Array[Byte]) {
}

object AX25Frame {

  val MaskUType = 0xEC
  val MaskFrameType: Int = 0x03
  val FrameTypeU: Int = 3
  val UTypeUi = 0x00

  def apply(data: Array[Byte]): AX25Frame = {
    var offset: Int = 1
    def callsign(pos: Int) = AX25Callsign(data.slice(pos, pos+7))

    val dest: AX25Callsign = callsign(offset)
    val sender: AX25Callsign = callsign(offset+7)

    val digipeaters: ListBuffer[AX25Callsign] = ListBuffer.empty
    offset = 15
    while ((data(offset-1) & 0x01) == 0) {
      digipeaters += callsign(offset)
      offset += 7
    }

    val ctl: Byte = data(offset)
    offset = offset + 1

    def pid(pos: Int): Option[Byte] = {
      if ((ctl & 0x01) == 0 || (ctl & (MaskUType | MaskFrameType)) == (UTypeUi | FrameTypeU)) {
        offset += 1
        Some(data(pos))
      } else {
        None
      }
    }
    val pidOpt: Option[Byte] = pid(offset)

    new AX25Frame(sender, dest, digipeaters.toArray, ctl, pidOpt, data.slice(offset, data.length))
  }

}