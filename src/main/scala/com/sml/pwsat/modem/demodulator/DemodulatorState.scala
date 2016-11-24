package com.sml.pwsat.modem.demodulator

object DemodulatorState extends Enumeration {
  type DemodulatorState = Value
  val WAITING = Value("WAITING")
  val JUST_SEEN_FLAG = Value("JUST_SEEN_FLAG")
  val DECODING = Value("DECODING")
}
