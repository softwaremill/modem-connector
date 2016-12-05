package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream, IOException}
import java.net.{Socket, SocketException}


class AGWPEHandler(socket: Socket) extends Runnable {

  val socketIn: DataInputStream = new DataInputStream(socket.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(socket.getOutputStream)

  send(socketOut, AGWPEFrame.version)
  send(socketOut, AGWPEFrame.info)
  send(socketOut, AGWPEFrame.monitorOn)

  override def run(): Unit = {
    while (true) {
      try {
        val frame: AGWPEFrame = AGWPEFrame(socketIn)
        val result: Char = (frame.dataKind & 0xFFFF).toChar
        println("RESULT: " + result)
        result match {
          case 'G' => handlePortInformationCommand(frame)
          case 'R' => handleAGWPEVersionCommand(frame)
          case 'g' => handleRadioPortCapabilities(frame)
          //todo: etc...
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
    send(socketOut, AGWPEFrame.valueOf('g'))
  }

  def handleRadioPortCapabilities(frame: AGWPEFrame): Unit = {

  }

  def handleAGWPEVersionCommand(frame: AGWPEFrame): Unit = {

  }

  def send(socketOut: DataOutputStream, agwpeFrame: AGWPEFrame): Unit = {
    this.synchronized {
      agwpeFrame.bytes.foreach(socketOut.write)
    }
  }
}
