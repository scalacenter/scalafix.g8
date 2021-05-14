import scala.sys.process._

lazy val root = (project in file("."))
  .settings(
    name := "scalafix.g8-root",
    Test / test := {
      val _ = (Test / g8Test).toTask("").value
      val s = streams.value
      s.log.info(
        "running our own sbt in the copied directory to mimic the scripted test"
      )
      val p = Process(
        Seq("sbt", "tests/test"),
        (Test / g8 / target).value / "scalafix"
      ).run()
      assert(p.exitValue() == 0, "Non-zero exit from sbt tests/test")
    },
    scriptedLaunchOpts ++= List(
      "-Xms1024m",
      "-Xmx1024m",
      "-XX:ReservedCodeCacheSize=128m",
      "-Xss2m",
      "-Dfile.encoding=UTF-8"
    )
  )
