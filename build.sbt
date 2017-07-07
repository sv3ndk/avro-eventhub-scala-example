resolvers in ThisBuild ++= Seq(
    Resolver.mavenLocal
)

name := "svend-playground-eventhub-avro-sender"

version := "0.1-SNAPSHOT"

organization := "org.svend.playground"

scalaVersion in ThisBuild := "2.12.2"

libraryDependencies ++= Seq(
  "com.microsoft.azure" % "azure-eventhubs" % "0.14.2",
  "org.apache.avro" % "avro" % "1.8.2"  
)
