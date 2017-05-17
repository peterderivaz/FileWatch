import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.11.8",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "FileWatch",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.scala-lang" % "scala-swing" % "2.11+",
    libraryDependencies += "com.beachape.filemanagement" %% "schwatcher" % "0.3.2"
  )
