name := "scala_future"

version := "1.0"

scalaVersion := "2.12.0"

libraryDependencies ++= Seq(
  "com.typesafe.akka" % "akka-actor_2.12.0-RC2" % "2.4.12",

  "org.scalatest" %% "scalatest" % "3.0.0" % "test",
  "com.typesafe.akka" % "akka-testkit_2.12.0-RC2" % "2.4.12" % "test"

)
