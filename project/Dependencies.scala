import sbt._

object Dependencies {
  val scalaTestVersion = "2.2.6"
  val scalaCheckVersion = "1.12.5"
  val akkaVersion = "2.4.7"

  val commonResolvers = Seq(
    "Sonatype repo" at "https://oss.sonatype.org/content/repositories/snapshots",
    Resolver.jcenterRepo
  )

  val coreDependencies = {
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVersion,
      "com.typesafe.akka" %% "akka-http-core" % akkaVersion,
      "com.typesafe.akka" %% "akka-stream" % akkaVersion,
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe" % "config" % "1.3.1",
      "com.softwaremill.akka-http-session" %% "core" % "0.2.5",
      "de.heikoseeberger" %% "akka-http-circe" % "1.5.2",
      "ch.megard" %% "akka-http-cors" % "0.1.1",
      "com.typesafe.akka" %% "akka-stream-contrib" % "0.2",
      "com.jsuereth" %% "scala-arm" % "1.4")
  }

  val testDependencies = {
    Seq("com.typesafe.akka" %% "akka-testkit" % "2.3.11",
      "org.scalatest" %% "scalatest" % scalaTestVersion,
      "org.scalacheck" %% "scalacheck" % scalaCheckVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaVersion
    ).map(_ % "test")
  }

}