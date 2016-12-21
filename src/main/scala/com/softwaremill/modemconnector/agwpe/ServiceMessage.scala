package com.softwaremill.modemconnector.agwpe

import ServiceMessage.ServiceMessageType

case class ServiceMessage(messageType: ServiceMessageType, content: String)

object ServiceMessage extends Enumeration {
  type ServiceMessageType = Value
  val Version, ConnectStatus, DisconnectStatus = Value
}