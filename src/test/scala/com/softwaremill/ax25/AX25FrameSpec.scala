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

  "An AX25Frame" should "contain digipiters" in {
    val ax24frame = FrameUtils.ax25frameFromFile("/complexFrame.bin")
    ax24frame.digipeaters.map(_.toString).toSeq should contain theSameElementsInOrderAs List("OZ6DIA-2", "OZ4DIA-2", "OZ4DIE-2")
    ax24frame.dest.toString should equal("U4TQ33")
    ax24frame.sender.toString should equal("OZ1IEP-9")
    new String(ax24frame.body) should equal("`'4< \\4>/]\"3k}145.500MHz qrv her=\r")
  }


}