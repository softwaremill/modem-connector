package com.softwaremill.service

trait Observer[S] {
  def receiveUpdate(subject: S)
}

