package com.sml.pwsat.modem

object SoundInterfaceType extends Enumeration {
  type SoundInterfaceType = Value
  val INPUT = Value("INPUT")
  val OUTPUT = Value("OUTPUT")
  val FILE_INPUT = Value("FILE_INPUT")
}
