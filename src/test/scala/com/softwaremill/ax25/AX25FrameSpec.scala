package com.softwaremill.ax25

import java.io.{DataInputStream, File, FileInputStream}

import com.softwaremill.agwpe.AGWPEFrame
import com.softwaremill.ax25.AX25Frame
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AX25FrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AX25Frame" should "contain decoded data" in {
    val ax24frame: AX25Frame = ax25frameFromFile("/andrej.bin")
    val decodedFrameBody = new String(ax24frame.body)
    decodedFrameBody should equal("=4603.63N/01431.26E-Op. Andrej")
  }

  "An AX25Frame" should "contain sender callsign" in {
    val ax24frame: AX25Frame = ax25frameFromFile("/andrej.bin")
    ax24frame.sender.callsign should equal("S57LN")
  }

  "An AX25Frame" should "contain destination callsign" in {
    val ax24frame: AX25Frame = ax25frameFromFile("/andrej.bin")
    ax24frame.dest.callsign should equal("APRS")
  }

  def ax25frameFromFile(filePath: String): AX25Frame = {
    val file: File = new File(this.getClass.getResource(filePath).getPath)
    AX25Frame(AGWPEFrame(new DataInputStream(new FileInputStream(file))).data.get)
  }

}