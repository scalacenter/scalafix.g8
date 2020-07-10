lazy val root = project
  .in(file("."))
  .settings(
    name := "hello-world",
    resolvers += Resolver.typesafeIvyRepo("releases"),
    test in Test := {
      val s = streams.value
      val _ = (g8Test in Test).toTask("").value
      val process = new ProcessBuilder("sbt", "tests/test")
        .directory(file("target/g8/scalafix"))
        .start()
      assert(process.exitValue() == 0, "Non-zero exit from sbt tests/test")
    },
    scriptedLaunchOpts ++= List(
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:ReservedCodeCacheSize=128m",
      "-Xss2m",
      "-Dfile.encoding=UTF-8"
    )
  )
