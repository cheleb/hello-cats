name := "hello-cats"
version := "0.0.1-SNAPSHOT"

scalaVersion := "2.13.6"

scalacOptions ++= Seq(
  "-encoding", "UTF-8",   // source files are in UTF-8
  "-deprecation",         // warn about use of deprecated APIs
  "-unchecked",           // warn about unchecked type parameters
  "-feature",             // warn about misused language features
   "-language:postfixOps",
  "-language:higherKinds",// allow higher kinded types without `import scala.language.higherKinds`
  "-Xlint",               // enable handy linter warnings
  "-Xfatal-warnings",     // turn compiler warnings into errors

)

libraryDependencies += "org.typelevel" %% "cats-core" % "2.6.1"
libraryDependencies += "org.typelevel" %% "cats-effect" % "3.2.2"

val circeVersion = "0.14.1"

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core",
  "io.circe" %% "circe-generic",
  "io.circe" %% "circe-parser"
).map(_ % circeVersion)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % Test
addCompilerPlugin("org.typelevel" % "kind-projector_2.13.6" % "0.13.0")

scalafmtOnCompile := true
