package com.softwaremill.modemconnector

trait Observer[S] {
  def receiveUpdate(subject: S)
}

