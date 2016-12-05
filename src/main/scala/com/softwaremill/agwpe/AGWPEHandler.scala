package com.softwaremill.agwpe

import java.io.{DataInputStream, DataOutputStream}
import java.net.Socket
import java.util.Date

import com.typesafe.scalalogging.LazyLogging


class AGWPEHandler(socket: Socket) extends Runnable with LazyLogging {

  val AgwpeFrameEncoding: String = "US-ASCII"
  val socketIn: DataInputStream = new DataInputStream(socket.getInputStream)
  val socketOut: DataOutputStream = new DataOutputStream(socket.getOutputStream)

  send(socketOut, AGWPEFrame.version)

  override def run(): Unit = {
    while (true) {
      try {
        val frame: AGWPEFrame = AGWPEFrame(socketIn)
        receiveCommand(frame)
      } catch {
        case e: Exception => logger.error("Error during AGWPE command reading.", e)
      }
    }
  }

  /**
    * Commands from application recognized:
    *
    * 'C' Connect status
    * 'd' Disconnect status
    * 'R' Request for version number.
    * 'K' Transmit raw AX.25 frame.
    * 'G' Ask about radio ports.
    * 'g' Capabilities of a port.
    * 'k' Ask to start receiving RAW AX25 frames.
    * 'm' Ask to start receiving Monitor AX25 frames.
    * 'V' Transmit UI data frame.
    * 'H' Report recently heard stations.
    * 'X' Register CallSign
    * 'x' Unregister CallSign
    * 'y' Ask Outstanding frames waiting on a Port
    *
    * A message is printed if any others are received.
    *
    * Messages sent to client application:
    *
    * 'R' Reply to Request for version number.
    * 'G' Reply to Ask about radio ports.
    * 'g' Reply to capabilities of a port.
    * 'K' Received AX.25 frame in raw format. (Enabled with 'k' command.)
    * 'U' Received AX.25 frame in monitor format. (Enabled with 'm' command.)
    * 'y' Outstanding frames waiting on a Port
    *
    * @param frame AGWPEFrame received
    */
  //noinspection ScalaStyle
  def receiveCommand(frame: AGWPEFrame): Unit = {
    val command: Char = (frame.dataKind & 0xFFFF).toChar
    logger.info("Command code received: " + command)
    command match {
      case 'C' => handleConnectStatusCommand(frame)
      case 'd' => handleDisconnectStatusCommand(frame)
      case 'R' => handleVersionCommand(frame)
      case 'K' => handleRawAX25FrameCommand(frame)
      case 'G' => logger.info("Ask about radio ports - Not Implemented")
      case 'g' => logger.info("Capabilities of a port - Not Implemented")
      case 'k' => logger.info("Ask to start receiving RAW AX25 frames - Not Implemented")
      case 'm' => logger.info("Ask to start receiving Monitor AX25 frames - Not Implemented")
      case 'V' => logger.info("Transmit UI data frame - Not Implemented")
      case 'H' => logger.info("Report recently heard stations - Not Implemented")
      case 'X' => logger.info("Register CallSign - Not Implemented")
      case 'x' => logger.info("Unregister CallSign - Not Implemented")
      case 'y' => logger.info("Ask Outstanding frames waiting on a Port - Not Implemented")
      case 'U' => logger.info("Received AX.25 frame in monitor format - Not Implemented")
      case _ => logger.error("Unknown command received in AGWPE Handler")
    }
  }

  private def handleConnectStatusCommand(frame: AGWPEFrame): Unit = {
    logger.info(new Date().toString + ": AGWPE port#" + frame.port +
      " connect to " + frame.callFrom + ": " + new String(frame.data.get, 0, frame.dataLength, AgwpeFrameEncoding))
  }

  private def handleDisconnectStatusCommand(frame: AGWPEFrame): Unit = {
    logger.info(new Date().toString + ": AGWPE port#" + frame.port +
      " disconnect from " + frame.callFrom + ": " + new String(frame.data.get, 0, frame.dataLength, AgwpeFrameEncoding))
  }

  //noinspection ScalaStyle
  private def handleVersionCommand(frame: AGWPEFrame): Unit = {
    val data: Array[Byte] = frame.data.get
    val majorVersion: Int = (data(0) & 0xFF) | ((data(1) & 0xFF) << 8) |
      ((data(2) & 0xFF) << 16) | ((data(3) & 0xFF) << 24)
    val minorVersion: Int = (data(4) & 0xFF) | ((data(5) & 0xFF) << 8) |
      ((data(6) & 0xFF) << 16) | ((data(7) & 0xFF) << 24)
    logger.info("AGWPE version " + majorVersion + '.' + minorVersion)
  }

  private def handleRawAX25FrameCommand(frame: AGWPEFrame): Unit = ???

  def send(socketOut: DataOutputStream, agwpeFrame: AGWPEFrame): Unit = {
    this.synchronized {
      agwpeFrame.bytes.foreach(socketOut.write)
    }
  }
}
