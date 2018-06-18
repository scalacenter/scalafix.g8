At the root of your scalafix.g8 generated build, run this migration

```
export _JAVA_OPTIONS=-Xss1515m # a large stack size is required for the Scala compiler
coursier launch ch.epfl.scala:scalafix-cli_2.12.6:0.6.0-M9 --main scalafix.cli.Cli -- -r github:scalacenter/scalafix.g8/v0.6 --verbose
```

The rewrite is syntactic and runs on both `*.scala` and `*.sbt` files.
The rule may not work as expected if you have customized the auto-generated
build.sbt and test suite.

Running the rewrite should produce a diff like this
```diff
diff --git a/scalafix/build.sbt b/scalafix/build.sbt
index 830ec17d..5f73b8e3 100644
--- a/scalafix/build.sbt
+++ b/scalafix/build.sbt
@@ -1,13 +1,22 @@
 // Use a scala version supported by scalafix.
 val V = _root_.scalafix.Versions
-scalaVersion in ThisBuild := V.scala212
+inThisBuild(
+  List(
+    scalaVersion := V.scala212,
+    addCompilerPlugin(scalafixSemanticdb),
+    scalacOptions += "-Yrangepos"
+  )
+)
 
 lazy val rules = project.settings(
   libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.version
 )
 
 lazy val input = project.settings(
-  scalafixSourceroot := sourceDirectory.in(Compile).value,
+  scalacOptions += {
+    val sourceroot = sourceDirectory.in(Compile).value / "scala"
+    s"-P:semanticdb:sourceroot:$sourceroot"
+  },
   libraryDependencies ++= Seq(
     "org.typelevel" %% "cats" % "0.9.0"
   ),
@@ -25,15 +34,13 @@ lazy val output = project.settings(
 lazy val tests = project
   .settings(
     libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.version % Test cross CrossVersion.full,
-    buildInfoPackage := "fix",
-    buildInfoKeys := Seq[BuildInfoKey](
-      "inputSourceroot" ->
-        sourceDirectory.in(input, Compile).value,
-      "outputSourceroot" ->
-        sourceDirectory.in(output, Compile).value,
-      "inputClassdirectory" ->
-        classDirectory.in(input, Compile).value
-    )
+    
+    scalafixTestkitOutputSourceDirectories :=
+      sourceDirectories.in(output, Compile).value,
+    scalafixTestkitInputSourceDirectories :=
+      sourceDirectories.in(input, Compile).value,
+    scalafixTestkitInputClasspath :=
+      fullClasspath.in(input, Compile).value
   )
   .dependsOn(input, rules)
-  .enablePlugins(BuildInfoPlugin)
+  .enablePlugins(ScalafixTestkitPlugin)
diff --git a/scalafix/project/plugins.sbt b/scalafix/project/plugins.sbt
index 20784f6c..f4587d04 100644
--- a/scalafix/project/plugins.sbt
+++ b/scalafix/project/plugins.sbt
@@ -1,4 +1,4 @@
-addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.5.2")
+addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.6.0-M9")
 addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-RC12")
-addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % "0.7.0")
+
 addSbtPlugin("org.lyranthe.sbt" % "partial-unification" % "1.1.0")
diff --git a/scalafix/rules/src/main/scala/fix/Cats_v1_0_0.scala b/scalafix/rules/src/main/scala/fix/Cats_v1_0_0.scala
index 814b60f8..3a9c3601 100644
--- a/scalafix/rules/src/main/scala/fix/Cats_v1_0_0.scala
+++ b/scalafix/rules/src/main/scala/fix/Cats_v1_0_0.scala
@@ -3,9 +3,9 @@ package v1_0_0
 
 import scalafix._
 import scalafix.syntax._
-import scalafix.util.SymbolMatcher
 import scala.meta._
 import scala.meta.contrib._
+import scalafix.v0._
 
 // ref: https://github.com/typelevel/cats/pull/1745
 case class RemoveCartesianBuilder(index: SemanticdbIndex)
diff --git a/scalafix/tests/src/test/scala/fix/Cats_Tests.scala b/scalafix/tests/src/test/scala/fix/Cats_Tests.scala
index e8e9d338..52210691 100644
--- a/scalafix/tests/src/test/scala/fix/Cats_Tests.scala
+++ b/scalafix/tests/src/test/scala/fix/Cats_Tests.scala
@@ -1,13 +1,9 @@
 package fix
 
 import scala.meta._
-import scalafix.testkit._
+import scalafix.v0._
 
 class Cats_Tests
-  extends SemanticRuleSuite(
-    Database.load(Classpath(AbsolutePath(BuildInfo.inputClassdirectory))),
-    AbsolutePath(BuildInfo.inputSourceroot),
-    Seq(AbsolutePath(BuildInfo.outputSourceroot))
-  ) {
+  extends scalafix.testkit.SemanticRuleSuite {
   runAllTests()
 }
```