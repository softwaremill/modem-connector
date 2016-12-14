package com.softwaremill.service

trait Subject[S] {
  private var observers: List[Observer[S]] = Nil

  def addObserver(observer: Observer[S]) = observers = observer :: observers

  def notifyObservers(obj: S) = observers.foreach(_.receiveUpdate(obj))

}