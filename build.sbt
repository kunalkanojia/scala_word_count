name := "scala_future"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.8",

  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.typesafe.akka" % "akka-testkit_2.11" % "2.4.8" % "test"

)
