organization := "de.le"

name := "Mensch-Aergere-Dich-Nicht-FX"

version := "1.0.0-SNAPSHOT"

scalaVersion := "2.10.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= Seq(
  "net.liftweb" %% "lift-json" % "2.5-M4",
  "net.liftweb" %% "lift-json-ext" % "2.5-M4",
  // "net.databinder.dispatch" % "dispatch-core_2.9.2" % "0.9.5"
  "net.databinder.dispatch" %% "dispatch-core" % "0.10.0",
  "org.scalafx" % "scalafx_2.10" % "1.0.0-M3",
  "com.typesafe.akka" %% "akka-actor" % "2.1.2",
  "com.typesafe.akka" %% "akka-remote" % "2.1.2"
 )
 
libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.2" % "test"

libraryDependencies += "junit" % "junit" % "4.11" % "test"
 
jfxSettings

JFX.mainClass := Some("ui.Main")

JFX.devKit := JFX.jdk("C:/Program Files/Java/jdk1.7.0_21")

JFX.addJfxrtToClasspath := true
 