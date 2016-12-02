package com.softwaremill.agwpe

import java.io.{DataInputStream, File, FileInputStream}

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPEFrameSpec extends FlatSpec with Matchers with BeforeAndAfter{

  "An AGWPEFrame" should "be created from binary data received from SoundModem" in {
    val dis:DataInputStream = new DataInputStream(new FileInputStream(file("/getInfoQuery.bin")))
    val frame:AGWPEFrame = AGWPEFrame(dis)
    frame.dataKind.toChar shouldEqual 'R'
  }

  "An AGWPEFrame" should "contain data received as encoded AX.25 frame" in {
    /*
      dataFrame.bin content:

      1:Fm ZL1AB To FBB <UI R Pid=F0 Len=158> [15:05:49R] [++-]
      541982 B   2718 SWPC  @WW     CX2SA  130424 Report of Solar-Geophysical Activi
      541983 B   1571 EQUAKE@WW     CX2SA  130424 NEW IRELAND REGION, PAPUA NEW GUIN
     */
    val dis:DataInputStream = new DataInputStream(new FileInputStream(file("/dataFrame.bin")))
    val frame:AGWPEFrame = AGWPEFrame(dis)
    new String(frame.data.get).contains("NEW IRELAND REGION") shouldBe true
  }

  def file(path: String): File = new File(this.getClass.getResource(path).getPath)
}
