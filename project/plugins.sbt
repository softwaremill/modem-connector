import sbt._

resolvers += "SoftwareMill Common" at "https://nexus.softwaremill.com/content/repositories/smlcommon-repos" //for rotten-todos

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.5.0")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("com.softwaremill.clippy" % "plugin-sbt" % "0.3.4")