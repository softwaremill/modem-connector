package com.softwaremill.modemconnector.ax25

import com.softwaremill.modemconnector.FrameUtils
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AX25FrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "A simple AX25Frame" should "contain decoded data" in {
    val ax25frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    new String(ax25frame.body) should equal("=4603.63N/01431.26E-Op. Andrej")
  }

  "A simple AX25Frame" should "contain sender callsign" in {
    val ax25frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    ax25frame.sender.callsign should equal("S57LN")
  }

  "A simple AX25Frame" should "contain destination callsign" in {
    val ax25frame: AX25Frame = FrameUtils.ax25frameFromFile("/andrej.bin")
    ax25frame.dest.callsign should equal("APRS")
  }

  "A simple AX25Frame" should "encode byte array" in {
    val ax25frame = AX25Frame(FrameUtils.ax25frameFromFile("/andrej.bin").toBytes)
    new String(ax25frame.body) should equal("=4603.63N/01431.26E-Op. Andrej")
    ax25frame.dest.callsign should equal("APRS")
    ax25frame.sender.callsign should equal("S57LN")
  }

  "An AX25Frame with digipiters" should "contain decoded data" in {
    val ax25frame = FrameUtils.ax25frameFromFile("/complexFrame.bin")
    new String(ax25frame.body) should equal("`'4< \\4>/]\"3k}145.500MHz qrv her=\r")
  }

  "An AX25Frame with digipiters" should "contain sender callsign" in {
    val ax25frame = FrameUtils.ax25frameFromFile("/complexFrame.bin")
    ax25frame.sender.toString should equal("OZ1IEP-9")
  }

  "An AX25Frame with digipiters" should "contain destination callsign" in {
    val ax25frame = FrameUtils.ax25frameFromFile("/complexFrame.bin")
    ax25frame.dest.toString should equal("U4TQ33")
  }

  "An AX25Frame with digipiters" should "contain digipiters" in {
    val ax25frame = FrameUtils.ax25frameFromFile("/complexFrame.bin")
    ax25frame.digipeaters.map(_.toString).toSeq should contain theSameElementsInOrderAs List("OZ6DIA-2", "OZ4DIA-2", "OZ4DIE-2")
  }

  "An AX25Frame with digipiters" should "encode byte array" in {
    val ax25frame = AX25Frame(FrameUtils.ax25frameFromFile("/complexFrame.bin").toBytes)
    ax25frame.digipeaters.map(_.toString).toSeq should contain theSameElementsInOrderAs List("OZ6DIA-2", "OZ4DIA-2", "OZ4DIE-2")
    ax25frame.dest.toString should equal("U4TQ33")
    ax25frame.sender.toString should equal("OZ1IEP-9")
    new String(ax25frame.body) should equal("`'4< \\4>/]\"3k}145.500MHz qrv her=\r")
  }

}