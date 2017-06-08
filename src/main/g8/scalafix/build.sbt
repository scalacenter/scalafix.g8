scalaVersion in ThisBuild := "2.12.2"

lazy val rewrites = project.settings(
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % "$scalafix_version$"
)

lazy val input = project.settings(
  scalametaSourceroot := sourceDirectory.in(Compile).value
)

lazy val output = project

lazy val tests = project
  .settings(
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % "$scalafix_version$" % Test cross CrossVersion.full,
    buildInfoPackage := "fix",
    buildInfoKeys := Seq[BuildInfoKey](
      "inputSourceroot" ->
        sourceDirectory.in(input, Compile).value,
      "outputSourceroot" ->
        sourceDirectory.in(output, Compile).value,
      "inputClassdirectory" ->
        classDirectory.in(input, Compile).value
    )
  )
  .dependsOn(input, rewrites)
  .enablePlugins(BuildInfoPlugin)
