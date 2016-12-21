import sbt._

object Dependencies {

  val commonResolvers = Seq(
    "Sonatype repo" at "https://oss.sonatype.org/content/repositories/snapshots",
    Resolver.jcenterRepo
  )

  val coreDependencies = {
    Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
      "ch.qos.logback" % "logback-classic" % "1.1.7",
      "com.typesafe" % "config" % "1.3.1"
    )
  }

  val testDependencies = {
    Seq(
      "org.scalatest" %% "scalatest" % "3.0.1",
      "org.scalacheck" %% "scalacheck" % "1.13.4"
    ).map(_ % "test")
  }

}