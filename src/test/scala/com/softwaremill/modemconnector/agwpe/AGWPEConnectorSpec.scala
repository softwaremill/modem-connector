package com.softwaremill.modemconnector.agwpe

import java.net.ServerSocket

import org.scalatest.{BeforeAndAfterEach, FlatSpec, Matchers}

class AGWPEConnectorSpec extends FlatSpec with Matchers with BeforeAndAfterEach {

  var server: ServerSocket = _

  override def beforeEach(): Unit = {
    server = new ServerSocket(AGWPESettings.port)
  }

  override def afterEach(): Unit = {
    server.close()
  }


  "An AGWPEConnector" should "be created with configuration settings as default" in {
    //when
    val connector: AGWPEConnector = new AGWPEConnector()
    //then
    connector.host shouldEqual "127.0.0.1"
    connector.port shouldEqual 8000
    connector.timeout shouldEqual 3000
  }

  "An AGWPEConnector" should "be created with configuration settings as constructor arguments" in {
    //given
    val host: String = "127.0.0.1"
    val port: Int = 8000
    val timeout: Int = 1000
    //when
    val connector: AGWPEConnector = new AGWPEConnector(host, port, timeout)
    //then
    connector.host shouldEqual host
    connector.port shouldEqual port
    connector.timeout shouldEqual timeout
  }
}