import sbt._
import sbt.Keys._

def haltOnCmdResultError(result: => Int) = if (result != 0) {
  throw new Exception("Build failed.")
}

import Dependencies._

lazy val commonSettings = Seq(
  organization := "com.softwaremill",
  version := "0.0.1-SNAPSHOT",
  scalaVersion := "2.11.8",
  resolvers ++= commonResolvers
) ++ Revolver.settings


lazy val root = (project in file("."))
  .settings(commonSettings: _*)
  .settings(
    name := "pwsat-modem",
    scalaSource in Compile := baseDirectory.value / "src/main/scala",
    scalaSource in Test := baseDirectory.value / "src/test/scala",
    libraryDependencies ++= coreDependencies ++ testDependencies,
    mainClass in Compile := Some("ApplicationMain"),
    assemblyJarName in assembly := "pwsat-modem-lib.jar",
    fork := true
  )