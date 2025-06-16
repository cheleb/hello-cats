name := "hello-cats"
version := "0.0.1-SNAPSHOT"

scalaVersion := "3.7.1"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
   "-language:postfixOps",
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
  "-Xfatal-warnings",     // turn compiler warnings into errors

)

run / fork := true

libraryDependencies += "org.typelevel" %% "cats-core" % "2.8.0"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.14"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.14"

val circeVersion = "0.14.1"
val tapirVersion = "1.9.10"
val http4sVersion = "0.23.26"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

// Tapir dependencies
libraryDependencies ++= Seq(
  "com.softwaremill.sttp.tapir" %% "tapir-core" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion
)

// Http4s dependencies
libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl" % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-ember-client" % http4sVersion
)

libraryDependencies ++= Seq(
  "io.opentelemetry" % "opentelemetry-exporter-otlp" % "1.49.0" % Runtime, // <1>
)

libraryDependencies ++= Seq(
  "org.typelevel" %% "log4cats-slf4j" % "2.7.0", // <1>
  "org.typelevel" %% "otel4s-oteljava" % "0.12.0", // <1>
  "org.typelevel" %% "otel4s-oteljava-context-storage" % "0.12.0", // <2>
  "ch.qos.logback" % "logback-classic" % "1.5.18", // <3>
)
javaOptions += "-Dcats.effect.trackFiberContext=true"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % Test
addCompilerPlugin("org.typelevel" % "kind-projector_2.13.6" % "0.13.2")

scalafmtOnCompile := true
