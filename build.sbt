val scala3Version = "3.8.4"

ThisBuild / scalaVersion := "3.8.4"
ThisBuild / name := "Oselka"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "org.psyd"

lazy val auth = (project in file("auth"))
  .settings(
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.7.0"
  )

lazy val app = (project in file("app"))
  .dependsOn(auth)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.13.25",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.13.25",
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "io.circe" %% "circe-core" % "0.14.15",
      "io.circe" %% "circe-generic" % "0.14.15",
      "io.circe" %% "circe-parser" % "0.14.15"
    )
  )

lazy val root = (project in file("."))
  .aggregate(auth, app)
