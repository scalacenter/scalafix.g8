lazy val root = project
  .in(file("."))
  .settings(
    name := "hello-world",
    test in Test := {
      val _ = (g8Test in Test).toTask("").value
      import sys.process._
      val log = ProcessLogger(println(_))
      Process(List("sbt", "tests/test"), cwd = Some(file("target/g8/scalafix")))
        .!!(log)
    },
    scriptedLaunchOpts ++= List("-Xms1024m",
                                "-Xmx1024m",
                                "-XX:ReservedCodeCacheSize=128m",
                                "-XX:MaxPermSize=256m",
                                "-Xss2m",
                                "-Dfile.encoding=UTF-8")
  )
