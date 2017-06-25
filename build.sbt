resolvers in ThisBuild ++= Seq(
    Resolver.mavenLocal
)

name := "svend-playground-eventhub-avro-sender"

version := "0.1-SNAPSHOT"

organization := "org.svend.playground"

scalaVersion in ThisBuild := "2.11.8"

libraryDependencies ++= Seq(
  "com.microsoft.azure" % "azure-eventhubs" % "0.14.0",
  "org.apache.avro" % "avro" % "1.8.2"  
)
