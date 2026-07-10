import sbtassembly.MergeStrategy

val scala3Version = "3.8.4"

ThisBuild / scalaVersion := "3.8.4"
ThisBuild / name := "Oselka"
ThisBuild / version := "0.1.0-SNAPSHOT"
ThisBuild / organization := "org.psyd"
ThisBuild / assembly / assemblyMergeStrategy := merge

val merge: String => MergeStrategy = x => {
  val PathList(path @ _*) = x
  path match {
    case init :+ "module-info.class" => MergeStrategy.discard
    case _ => MergeStrategy.defaultMergeStrategy(x)
  }
}

lazy val game = (project in file("game"))

lazy val auth = (project in file("auth"))
  .settings(
    libraryDependencies ++= Seq(
      "com.github.f4b6a3" % "uuid-creator" % "6.1.1",
      "org.typelevel" %% "cats-effect" % "3.7.0"
    )
  )

lazy val infrastructure = (project in file("infrastructure"))
  .dependsOn(game, auth)
  .settings(
    libraryDependencies ++= Seq(
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "org.typelevel" %% "doobie-core" % "1.0.0-RC13",
      "org.typelevel" %% "doobie-postgres" % "1.0.0-RC13",
      "org.typelevel" %% "doobie-specs2" % "1.0.0-RC13",
      "org.typelevel" %% "doobie-hikari" % "1.0.0-RC13",
      "de.mkammerer" % "argon2-jvm" % "2.12",
      "com.auth0" % "java-jwt" % "4.5.2"
    )
  )

lazy val app = (project in file("app"))
  .dependsOn(game, infrastructure, auth)
  .settings(
    libraryDependencies ++= Seq(
      "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.13.25",
      "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.13.25",
      "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.13.25",
      "org.http4s" %% "http4s-ember-server" % "0.23.34",
      "org.http4s" %% "http4s-dsl" % "0.23.34",
      "org.typelevel" %% "cats-effect" % "3.7.0",
      "io.circe" %% "circe-core" % "0.14.15",
      "io.circe" %% "circe-generic" % "0.14.15",
      "io.circe" %% "circe-parser" % "0.14.15"
    ),
    assembly / mainClass := Some("org.psyd.oselka.app.Application")
  )

lazy val root = (project in file("."))
  .aggregate(auth, app)
