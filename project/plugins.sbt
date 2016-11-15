import sbt._

resolvers += "SoftwareMill Common" at "https://nexus.softwaremill.com/content/repositories/smlcommon-repos" //for rotten-todos

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.3")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.8.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.8.0")

addSbtPlugin("com.softwaremill.clippy" % "plugin-sbt" % "0.2.5")