lazy val V = _root_.scalafix.sbt.BuildInfo
// Use a scala version supported by scalafix.
inThisBuild(
  List(
    scalaVersion := V.scala212,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions += "-Yrangepos"
  )
)

lazy val rules = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafix
)

lazy val input = project.settings(
  scalaVersion := "2.12.6",
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit_2.12.4" % "0.5.10"
)

lazy val output = project.settings(
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafix cross CrossVersion.full
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafix % Test cross CrossVersion.full,
    scalafixTestkitOutputSourceDirectories :=
      unmanagedSourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      unmanagedSourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value,
    compile.in(Compile) := compile
      .in(Compile)
      .dependsOn(compile.in(input, Compile))
      .value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
