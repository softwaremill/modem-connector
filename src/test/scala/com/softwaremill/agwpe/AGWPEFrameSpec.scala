package com.softwaremill.agwpe

import java.io.{DataInputStream, File, FileInputStream}

import com.softwaremill.FrameUtils
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AGWPEFrame" should "be created from binary data received from SoundModem" in {
    val dis: DataInputStream = FrameUtils.dataStream("/getInfoQuery.bin")
    val frame: AGWPEFrame = AGWPEFrame(dis)
    frame.command shouldEqual 'R'
  }

  "An AGWPEFrame" should "contain data received as encoded AX.25 frame" in {
    /*
      dataFrame.bin content:
      541982 B   2718 SWPC  @WW     CX2SA  130424 Report of Solar-Geophysical Activi
      541983 B   1571 EQUAKE@WW     CX2SA  130424 NEW IRELAND REGION, PAPUA NEW GUIN
     */
    val dis: DataInputStream = FrameUtils.dataStream("/dataFrame.bin")
    val frame: AGWPEFrame = AGWPEFrame(dis)
    val data = new String(frame.data.get)
    data should include("541982")
    data should include("PAPUA NEW GUIN")
  }

}
