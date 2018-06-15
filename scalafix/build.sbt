lazy val V = _root_.scalafix.Versions
// Use a scala version supported by scalafix.
inThisBuild(
  List(
    scalaVersion := V.scala212,
    addCompilerPlugin(scalafixSemanticdb),
    scalacOptions += "-Yrangepos"
  )
)

lazy val rules = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.version
)

lazy val input = project.settings(
  scalacOptions += {
    val sourceroot = sourceDirectory.in(Compile).value / "scala"
    s"-P:semanticdb:sourceroot:$sourceroot"
  },
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % "0.5.10" cross CrossVersion.full
)

lazy val output = project.settings(
  libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.version cross CrossVersion.full
)

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.version % Test cross CrossVersion.full,
    buildInfoPackage := "fix",
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value,
    buildInfoKeys := Seq[BuildInfoKey](
      "inputSourceroot" ->
        sourceDirectory.in(input, Compile).value,
      "outputSourceroot" ->
        sourceDirectory.in(output, Compile).value,
      "inputClassdirectory" ->
        classDirectory.in(input, Compile).value
    )
  )
  .dependsOn(input, rules)
  .enablePlugins(BuildInfoPlugin, ScalafixTestkitPlugin)
