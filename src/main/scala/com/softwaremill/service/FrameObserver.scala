package com.softwaremill.service

trait FrameObserver[S] {
  def receiveUpdate(subject: S)
}

