package com.sml.pwsat.modem

import javax.sound.sampled.{SourceDataLine, TargetDataLine}

import org.scalatest.{BeforeAndAfter, FlatSpec, Matchers}

class SoundInterfaceSpec extends FlatSpec with Matchers with BeforeAndAfter {

  "A sound interface factory" should "return InputSoundInterface instance" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.INPUT, interfaceName)
    sif shouldBe a[InputSoundInterface]
    sif.getDataLine shouldBe a[Some[TargetDataLine]]
  }

  "A sound interface factory" should "return OutputSoundInterface instance" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, interfaceName)
    sif shouldBe a[OutputSoundInterface]
    sif.getDataLine shouldBe a[Some[SourceDataLine]]
  }

  "A sound interface factory" should "return None for unknown interface name and OUTPUT type" in {
    val wrongInterfaceName: String = "Wrong-Interface-Name"
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, wrongInterfaceName)
    sif.getDataLine shouldBe a[Option[Nothing]]
  }

  "A sound interface factory" should "return None for unknown interface name and INPUT type" in {
    val wrongInterfaceName: String = "Wrong-Interface-Name"
    val sif = SoundInterfaceFactory(SoundInterfaceType.INPUT, wrongInterfaceName)
    sif.getDataLine shouldBe a[Option[Nothing]]
  }

  "A sound interface instance" should "return proper string for its interfaceName" in {
    val interfaceName = SoundInterface.getSoundInterfacesNames.head
    val sif = SoundInterfaceFactory(SoundInterfaceType.OUTPUT, interfaceName)
    sif.getDataLine shouldBe a[Some[SourceDataLine]]
    sif.toString shouldBe "Interface Name: " + interfaceName
  }

}