lazy val root = project
  .in(file("."))
  .settings(
    name := "scala3-akka-http-server",
    version := "0.1.0",
    scalaVersion := "2.13.12",
    libraryDependencies ++= Seq (
      "com.typesafe.akka" %% "akka-actor-typed" % "2.6.14",
      "com.typesafe.akka" %% "akka-stream" % "2.6.14",
      "com.typesafe.akka" %% "akka-http" % "10.2.4",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.4",
      "ch.megard" %% "akka-http-cors" % "1.1.1",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.2.4" % Test,
      "org.scalatest" %% "scalatest" % "3.2.9" % Test
    )
  )
