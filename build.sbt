name := "forecast-io-api"

organization := "com.film42"

version := "0.0.1-GEC-SNAPSHOT"

scalaVersion := "2.10.4"

resolvers += "spray" at "http://repo.spray.io/"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.10" % "2.0" % "test" withSources() withJavadoc(),
  "org.scalacheck" %% "scalacheck" % "1.10.0" % "test" withSources() withJavadoc(),
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.0",
  "io.spray" %%  "spray-json" % "1.2.6",
  "com.eclipsesource.minimal-json" % "minimal-json" % "0.9.4"
)

initialCommands := "import com.film42.forecastioapi._"

scalacOptions += "-deprecation"

publishMavenStyle := true

credentials := Seq(Credentials( Path.userHome / ".ivy2" / ".credentials"))

pomIncludeRepository := { _: MavenRepository => false }

publishTo := Some("snapshots" at "https://repo.totalgrid.org/artifactory/third-party-snapshot")

pomExtra := {
  <url>https://github.com/gec/forecast-io-scala.git</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://www.apache.org/licenses/LICENSE-2.0.html</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:gec/forecast-io-scala.git</url>
      <connection>scm:git:git@github.com:gec/forecast-io-scala.git</connection>
    </scm>
}
