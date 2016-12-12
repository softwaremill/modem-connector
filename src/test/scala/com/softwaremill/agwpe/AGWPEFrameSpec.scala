package com.softwaremill.agwpe

import java.io.{DataInputStream, File, FileInputStream}

import com.softwaremill.FrameUtils
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AGWPEFrame" should "be created from binary data received from SoundModem" in {
    //given
    val dis: DataInputStream = FrameUtils.dataStream("/getInfoQuery.bin")
    //when
    val frame: AGWPEFrame = AGWPEFrame(dis)
    //then
    frame.command shouldEqual 'R'
  }

  "An AGWPEFrame" should "contain data received as encoded AX.25 frame" in {
    //given
    /*
      dataFrame.bin content:
      541982 B   2718 SWPC  @WW     CX2SA  130424 Report of Solar-Geophysical Activi
      541983 B   1571 EQUAKE@WW     CX2SA  130424 NEW IRELAND REGION, PAPUA NEW GUIN
     */
    val dis: DataInputStream = FrameUtils.dataStream("/dataFrame.bin")
    val frame: AGWPEFrame = AGWPEFrame(dis)
    //when
    val data = new String(frame.data.get)
    //then
    data should include("541982")
    data should include("PAPUA NEW GUIN")
  }

  "An AGWPEFrame" should "be created for Version command type" in {
    //when
    val frame: AGWPEFrame = AGWPEFrame.versionFrame
    //then
    frame.command shouldEqual 'R'
    frame.port shouldEqual 0
    frame.pid shouldEqual 0
    frame.callTo shouldEqual None
    frame.callFrom shouldEqual None
    frame.dataLength shouldEqual 0
    frame.data shouldEqual None
  }

  "An AGWPEFrame" should "be created for Monitor On command type" in {
    //when
    val frame: AGWPEFrame = AGWPEFrame.monitorOnFrame
    //then
    frame.command shouldEqual 'k'
    frame.port shouldEqual 0
    frame.pid shouldEqual 0
    frame.callTo shouldEqual None
    frame.callFrom shouldEqual None
    frame.dataLength shouldEqual 0
    frame.data shouldEqual None
  }

}
