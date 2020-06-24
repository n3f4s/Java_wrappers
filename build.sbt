lazy val app = project
  .in(file("."))
  .settings(
    scalacOptions ++= Seq(
      "-language:postfixOps",
      "-language:implicitConversions",
    ),
    libraryDependencies ++= Seq(),
    name := "Java-Wrapper",
  )
