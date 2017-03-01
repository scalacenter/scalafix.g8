scalaVersion in ThisBuild := "2.11.8"

lazy val rewriteMe = project

lazy val rewritesHere = project
  .settings(
    libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % "0.3.1"
  )
  .dependsOn(
    rewriteMe % Scalameta
  )
