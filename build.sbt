name := "akka-classic-testkit-sandbox"

version := "0.1"

scalaVersion := "2.13.3"

val AkkaVersion = "2.6.8"
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.2.0" % Test,
  "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
)