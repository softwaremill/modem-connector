package com.softwaremill.agwpe

import com.typesafe.config.ConfigFactory

object AGWPESettings {
  val conf = ConfigFactory.load()
  lazy val host: String = conf.getString("agwpe.host")
  lazy val port: Int = conf.getInt("agwpe.port")
  lazy val timeout: Int = conf.getInt("agwpe.timeout")
}
