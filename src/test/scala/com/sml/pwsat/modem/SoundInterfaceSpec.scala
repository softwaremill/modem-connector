package com.sml.pwsat.modem

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class SoundInterfaceSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "A sound interface factory" should "return InputSoundInterface instance" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.INPUT, interfaceName)
    sif shouldBe a[InputSoundInterface]
    sif.getDataLine shouldBe a[Some[_]]
  }

  "A sound interface factory" should "return OutputSoundInterface instance" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, interfaceName)
    sif shouldBe a[OutputSoundInterface]
    sif.getDataLine shouldBe a[Some[_]]
  }

  "A sound interface factory" should "return None for unknown interface name and OUTPUT type" in {
    val wrongInterfaceName: String = "Wrong-Interface-Name"
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, wrongInterfaceName)
    sif.getDataLine should be (None)
  }

  "A sound interface factory" should "return None for unknown interface name and INPUT type" in {
    val wrongInterfaceName: String = "Wrong-Interface-Name"
    val sif = SoundInterfaceFactory(SoundInterfaceType.INPUT, wrongInterfaceName)
    sif.getDataLine should be (None)
  }

  "A sound interface instance" should "return proper string for its interfaceName" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, interfaceName)
    sif.getDataLine shouldBe a[Some[_]]
    sif.toString shouldBe "System Interface Name: " + interfaceName
  }

}