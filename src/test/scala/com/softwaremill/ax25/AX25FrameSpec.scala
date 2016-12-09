package com.softwaremill.ax25

import com.softwaremill.FrameUtils
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AX25FrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AX25Frame" should "contain decoded data" in {
    val ax24frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    val decodedFrameBody = new String(ax24frame.body)
    decodedFrameBody should equal("=4603.63N/01431.26E-Op. Andrej")
  }

  "An AX25Frame" should "contain sender callsign" in {
    val ax24frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    ax24frame.sender.callsign should equal("S57LN")
  }

  "An AX25Frame" should "contain destination callsign" in {
    val ax24frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    ax24frame.dest.callsign should equal("APRS")
  }

  "An AX25Frame" should "encode byte array" in {
    val ax24frame = AX25Frame(FrameUtils.ax25frameFromFile("/andrej.bin").toBytes)
    new String(ax24frame.body) should equal("=4603.63N/01431.26E-Op. Andrej")
    ax24frame.dest.callsign should equal("APRS")
    ax24frame.sender.callsign should equal("S57LN")
  }


}