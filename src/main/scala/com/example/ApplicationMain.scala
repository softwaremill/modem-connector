package com.example

import java.io.File
import java.util.concurrent.{BlockingQueue, LinkedBlockingQueue}
import javax.sound.sampled.{AudioFormat, TargetDataLine}

import akka.actor.ActorSystem
import com.sml.pwsat.modem._

object ApplicationMain extends App {
  //  val system = ActorSystem("MyActorSystem")
  //  val pingActor = system.actorOf(PingActor.props, "pingActor")
  //  pingActor ! PingActor.Initialize
  //  /*
  //   * This example app will ping pong 3 times and thereafter terminate the ActorSystem -
  //   * see counter logic in PingActor
  //   */
  //  system.awaitTermination()

  val file = new File(getClass.getResource("/packet12.wav").getPath)
//    val interface: SoundInterface = SoundInterfaceFactory.apply(SoundInterfaceType.INPUT, "Soundflower (2ch)")
//  val interface: SoundInterface = SoundInterfaceFactory.apply(SoundInterfaceType.FILE_INPUT, file.getAbsolutePath)
//  val line: TargetDataLine = interface.getDataLine.get.asInstanceOf[TargetDataLine]
//  val format: AudioFormat = interface.getAudioFormat
  val queue: BlockingQueue[Double] = new LinkedBlockingQueue[Double]
//  val producer: LineDecodingSampleProducer = new LineDecodingSampleProducer(line, format, queue)
  val producer: FileDecodingSampleProducer = new FileDecodingSampleProducer(file, queue)
  val consumer: PrintSampleConsumer = new PrintSampleConsumer(queue)
  new Thread(producer).start()
  new Thread(consumer).start()
}