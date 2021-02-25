lazy val app = project
  .in(file("."))
  .settings(
    scalacOptions ++= Seq(
      "-language:postfixOps",
      "-language:implicitConversions",
    ),
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.1.0" % "test",
      ),
    organization := "org.nefas",
    version      := "0.1-SNAPSHOT",
    name := "java_wrapper",
  )
