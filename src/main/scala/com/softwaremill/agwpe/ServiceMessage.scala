package com.softwaremill.agwpe

import com.softwaremill.agwpe.ServiceMessage.ServiceMessageType

case class ServiceMessage(messageType: ServiceMessageType, content: String)

object ServiceMessage extends Enumeration {
  type ServiceMessageType = Value
  val Version, ConnectStatus, DisconnectStatus = Value
}