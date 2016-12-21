package com.softwaremill.modemconnector.agwpe

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class AGWPESettingsSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "An AGWPESettings object" should "be created from configuraton file" in {
    //when
    AGWPESettings.host shouldEqual "127.0.0.1"
    AGWPESettings.port shouldEqual 8000
    AGWPESettings.timeout shouldEqual 3000
  }
}
