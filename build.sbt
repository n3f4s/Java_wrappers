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
    name := "Java-Wrapper",
  )
