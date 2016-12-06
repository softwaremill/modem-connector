package com.softwaremill.service

trait Subject[S] {
  private var observers: List[FrameObserver[S]] = Nil

  def addObserver(observer: FrameObserver[S]) = observers = observer :: observers

  def notifyObservers(frame: S) = observers.foreach(_.receiveUpdate(frame))

}