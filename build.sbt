import sbt._
import Dependencies._

  val ScalaVersion = "2.12.1"

  lazy val commonSettings = Seq(
    organization := "com.softwaremill.modem-connector",
    version := "0.1.0-SNAPSHOT",
    crossScalaVersions := Seq("2.11.8", "2.12.1"),
    scalaVersion := ScalaVersion,
    resolvers ++= commonResolvers,
    // Sonatype OSS deployment
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (version.value.trim.endsWith("SNAPSHOT")) {
        Some("snapshots" at nexus + "content/repositories/snapshots")
      } else {
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
      }
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra :=
      <scm>
        <url>https://github.com/softwaremill/modem-connector.git</url>
        <connection>scm:git:git@github.com:softwaremill/modem-connector.git</connection>
      </scm>
        <developers>
          <developer>
            <id>softberries</id>
            <name>Krzysztof Grajek</name>
          </developer>
          <developer>
            <id>tkluczak</id>
            <name>Tomasz Luczak</name>
          </developer>
        </developers>,
    licenses := ("Apache2", new java.net.URL("http://www.apache.org/licenses/LICENSE-2.0.txt")) :: Nil,
    homepage := Some(new java.net.URL("http://www.softwaremill.com"))
  ) ++ Revolver.settings

  lazy val root = (project in file("."))
    .settings(commonSettings: _*)
    .settings(
      name := "modem-connector",
      scalaSource in Compile := baseDirectory.value / "src/main/scala",
      scalaSource in Test := baseDirectory.value / "src/test/scala",
      libraryDependencies ++= coreDependencies ++ testDependencies,
      fork := true
    )

  def haltOnCmdResultError(result: => Int) = if (result != 0) {
    throw new Exception("Build failed.")
  }