lazy val V = _root_.scalafix.sbt.BuildInfo
inThisBuild(
  List(
    scalaVersion := V.scala213,
    crossScalaVersions := List(V.scala213, V.scala212, V.scala211),
    organization := "com.example",
    homepage := Some(url("https://github.com/com/example")),
    licenses := List(
      "Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")
    ),
    developers := List(
      Developer(
        "example-username",
        "Example Full Name",
        "example@email.com",
        url("https://example.com")
      )
    ),
    semanticdbEnabled := true,
    semanticdbVersion := scalafixSemanticdb.revision,
    scalacOptions ++= List(
      "-P:semanticdb:synthetics:on"
    )
  )
)

publish / skip := true

lazy val rules = project.settings(
  moduleName := "scalafix",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  publish / skip := true
)

lazy val output = project.settings(
  publish / skip := true
)

lazy val tests = project
  .settings(
    publish / skip := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    Compile / compile :=
      (Compile / compile).dependsOn(input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / unmanagedSourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value,
    scalafixTestkitInputScalacOptions :=
      (input / Compile / scalacOptions).value,
    scalafixTestkitInputScalaVersion :=
      (input / Compile / scalaVersion).value
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
