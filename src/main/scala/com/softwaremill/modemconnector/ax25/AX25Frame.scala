package com.softwaremill.modemconnector.ax25

class AX25Frame(val sender: AX25Callsign, val dest: AX25Callsign,
                val digipeaters: Array[AX25Callsign],
                val ctl: Byte,
                val pid: Option[Byte],
                val body: Array[Byte]) {

  val Flag: Byte = 0x7E

  def toBytes: Array[Byte] = {
    (Seq(Flag) ++ dest.toBytes ++ sender.toBytes ++ digipeaters.flatMap(_.toBytes) ++ Seq(ctl) ++ pid.toSeq ++ body).toArray
  }

  override def toString: String = {
    def toHexString(data: Int): String = Integer.toHexString(data & 0xFF)
    "AX25Frame[" + "from=" + sender + ", to=" + dest + digipeaters.mkString(", digipeaters=[", ", ", "]") +
      ", ctl=" + toHexString(ctl) + pid.map(pidValue => ", pid=" + toHexString(pidValue)).getOrElse("") +
      Option(body).map(bodyBytes => ", " + new String(bodyBytes)).getOrElse(", no body") + "]"
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

    def retriveDigipeaters(offset: Int): List[AX25Callsign] = {
      if ((data(offset - 1) & 0x01) == 0) callsign(offset) :: retriveDigipeaters(offset + CallsignSize) else Nil
    }

    val digipeatersPos: Int = senderPos + CallsignSize
    val digipeaters: List[AX25Callsign] = retriveDigipeaters(digipeatersPos)

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
    val pidOpt: Option[Byte] = pid(pidOptPos)

    implicit def bool2int(b: Boolean) = if (b) 1 else 0

    val dataPos: Int = pidOptPos + pidOpt.nonEmpty
    new AX25Frame(sender, dest, digipeaters.toArray, ctl, pidOpt, data.slice(dataPos, data.length))
  }

}