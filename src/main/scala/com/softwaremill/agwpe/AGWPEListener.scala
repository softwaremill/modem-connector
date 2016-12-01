package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream, IOException}
import java.net.{Socket, SocketException}


class AGWPEListener(socket: Socket) extends Runnable {

  val socketIn: DataInputStream = new DataInputStream(socket.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(socket.getOutputStream)

  Sender.sendAGWPEFrame(socketOut, 0, 'R', 0, null, null, null) // ask for version
  Sender.sendAGWPEFrame(socketOut, 0, 'G', 0, null, null, null) // ask for radioport info
  Sender.sendAGWPEFrame(socketOut, 0, 'k', 0, null, null, null)

  override def run(): Unit = {
    while (true) {
      try {
        val frame: AGWPEFrame = AGWPEFrameHandler.handleAGWPEFrame(socketIn)
        val result: Char = (frame.dataKind & 0xFFFF).toChar
        println("RESULT: " + result)
        result match {
          case 'G' => handlePortInformationCommand(frame)
          case 'R' => handleAGWPEVersionCommand(frame)
          case 'g' => handleRadioPortCapabilities(frame)
          //etc...
          case _ => println("Unknown command")

        }
      } catch {
        case se: SocketException => println(se.getMessage)
        case ioe: IOException => println(ioe.getMessage)
        case all: Throwable => println(all.getMessage)
      }
    }
  }

  def handlePortInformationCommand(frame: AGWPEFrame): Unit = {
    Sender.sendAGWPEFrame(socketOut, 0, 'g', 0, null, null, null)
  }

  def handleRadioPortCapabilities(frame: AGWPEFrame): Unit = {

  }

  def handleAGWPEVersionCommand(frame: AGWPEFrame): Unit = {

  }
}
