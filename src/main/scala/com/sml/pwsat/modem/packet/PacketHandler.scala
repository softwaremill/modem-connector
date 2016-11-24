package com.sml.pwsat.modem.packet

class PacketHandler {
  def handlePacket(bytes: Array[Byte]): Unit = {
    println(AX25Packet.format(bytes))
  }
}
