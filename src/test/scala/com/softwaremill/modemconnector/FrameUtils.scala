package com.softwaremill.modemconnector

import java.io.{DataInputStream, File, FileInputStream}

import com.softwaremill.modemconnector.agwpe.AGWPEFrame
import com.softwaremill.modemconnector.ax25.AX25Frame

object FrameUtils {

  def ax25frameFromFile(filePath: String): AX25Frame = {
    val file: File = new File(this.getClass.getResource(filePath).getPath)
    AX25Frame(AGWPEFrame(new DataInputStream(new FileInputStream(file))).data.get)
  }

  def dataStream(filePath: String): DataInputStream = {
    val file: File = new File(this.getClass.getResource(filePath).getPath)
    new DataInputStream(new FileInputStream(file))
  }
}
