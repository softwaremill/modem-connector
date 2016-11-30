package com.softwaremill.agwpe

import java.net.{InetSocketAddress, Socket}


class AGWPEConnector {

  def open(): Unit = {
    val sockToAGWPE: Socket = new Socket
    sockToAGWPE.setSoLinger(false, 1)
    sockToAGWPE.connect(new InetSocketAddress(AGWPESettings.host, AGWPESettings.port), AGWPESettings.timeout)
    val listener: AGWPEListener = new AGWPEListener(sockToAGWPE, AGWPESettings.host, AGWPESettings.port)
    val t: Thread = new Thread(listener, "AGWPEConnector[" + AGWPESettings.host + ':' + AGWPESettings.port + ']')
    t.setDaemon(false)
    t.start()

    listener.sendAGWPEFrame(0, 'R', 0, null, null, null) // ask for version
    listener.sendAGWPEFrame(0, 'G', 0, null, null, null) // ask for radioport info
  }
}
