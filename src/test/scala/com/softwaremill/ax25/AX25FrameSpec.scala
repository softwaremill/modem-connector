package com.softwaremill.ax25

import java.io.{DataInputStream, File, FileInputStream}

import com.softwaremill.agwpe.AGWPEFrame
import com.softwaremill.ax25.AX25Frame
import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AX25FrameSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AX25Frame" should "contain decoded data" in {
    val ax24frame: AX25Frame = AX25Frame(AGWPEFrame(dataStream("/andrej.bin")).data.get)
    val decodedFrameBody = new String(ax24frame.body)
    decodedFrameBody should equal("=4603.63N/01431.26E-Op. Andrej")
  }

  def dataStream(filePath: String): DataInputStream = {
    val file: File = new File(this.getClass.getResource(filePath).getPath)
    new DataInputStream(new FileInputStream(file))
  }

}