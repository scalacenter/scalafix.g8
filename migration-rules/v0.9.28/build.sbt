lazy val V = _root_.scalafix.sbt.BuildInfo

// Use a scala version supported by scalafix.
inThisBuild(
  List(
    scalaVersion := V.scala213,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions += "-Yrangepos"
  )
)

lazy val rules = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  (publish / skip) := true,
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion cross CrossVersion.full
)
lazy val output = project.settings(
  (publish / skip) := true,
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % "0.9.27+81-ab0c497d-SNAPSHOT" cross CrossVersion.full // move to 0.9.28 once released
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value,
    Compile / compile := (Compile / compile)
      .dependsOn(input / Compile / compile)
      .value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
