package com.softwaremill.ax25

class AX25Frame(val sender: AX25Callsign, val dest: AX25Callsign,
                val digipeaters: Array[AX25Callsign],
                val ctl: Byte,
                val pid: Option[Byte],
                val body: Array[Byte]) {

  val Flag: Byte = 0x7E

  def toBytes: Array[Byte] = {
    (Array(Flag) :: dest.toBytes :: sender.toBytes :: digipeaters.flatMap(_.toBytes) :: Array(ctl) :: pid.toArray :: body :: Nil).toArray.flatten
  }

}

object AX25Frame {

  val MaskUType = 0xEC
  val MaskFrameType: Int = 0x03
  val FrameTypeU: Int = 3
  val UTypeUi = 0x00

  val CallsignSize: Int = 7
  val Offset: Int = 1

  def apply(data: Array[Byte]): AX25Frame = {
    def callsign(pos: Int) = AX25Callsign(data.slice(pos, pos + CallsignSize))

    val dest: AX25Callsign = callsign(Offset)
    val senderPos: Int = Offset + CallsignSize
    val sender: AX25Callsign = callsign(senderPos)

    def retriveDigipeaters(offset: Int, digipeaters: List[AX25Callsign]): List[AX25Callsign] = {
      if ((data(offset - 1) & 0x01) == 0) {
        retriveDigipeaters(offset + CallsignSize, digipeaters) ++ digipeaters
      } else {
        digipeaters
      }
    }

    val digipeatersPos: Int = senderPos + CallsignSize
    val digipeaters: List[AX25Callsign] = retriveDigipeaters(digipeatersPos, List.empty)

    val ctlPos: Int = digipeatersPos + digipeaters.size * CallsignSize
    val ctl: Byte = data(ctlPos)

    def pid(pos: Int): Option[Byte] = {
      if ((ctl & 0x01) == 0 || (ctl & (MaskUType | MaskFrameType)) == (UTypeUi | FrameTypeU)) {
        Some(data(pos))
      } else {
        None
      }
    }

    val pidOptPos: Int = ctlPos + 1
    val pidOpt: Option[Byte] = pid(Offset + 2 * CallsignSize + digipeaters.size * Offset + 1)

    implicit def bool2int(b: Boolean) = if (b) 1 else 0

    val dataPos: Int = pidOptPos + pidOpt.nonEmpty
    new AX25Frame(sender, dest, digipeaters.toArray, ctl, pidOpt, data.slice(dataPos, data.length))
  }

  override def toString: String = super.toString

  def toBytes: Array[Byte] = ???
}