package fix

import scala.collection.mutable
import scalafix.v0._
import scala.meta._

object Scalafixg8_v0_6 extends Rule("Scalafix_v0_6") {

  override def fix(ctx: RuleCtx): Patch = {
    val imports = mutable.Map.empty[String, Importer]
    var patch = Patch.empty
    def addImporter(importer: Importer, importees: List[Importee]): Unit = {
      imports(importer.syntax) = importer
      importees.foreach { i =>
        patch += ctx.removeImportee(i)
      }
    }

    ctx.tree.traverse {
      case q""" $_.enablePlugins(${t @ q"BuildInfoPlugin"})""" =>
        patch += ctx.replaceTree(t, "ScalafixTestkitPlugin")
      case t @ q"""
        buildInfoKeys := Seq[BuildInfoKey](
          "inputSourceroot" ->
            sourceDirectory.in(input, Compile).value,
          "outputSourceroot" ->
            sourceDirectory.in(output, Compile).value,
          "inputClassdirectory" ->
            classDirectory.in(input, Compile).value
        )
        """ =>
        patch += ctx.replaceTree(
          t,
          """scalafixTestkitOutputSourceDirectories :=
            |      sourceDirectories.in(output, Compile).value,
            |    scalafixTestkitInputSourceDirectories :=
            |      sourceDirectories.in(input, Compile).value,
            |    scalafixTestkitInputClasspath :=
            |      fullClasspath.in(input, Compile).value""".stripMargin
        )
      case t @ q""" addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % ${_: Lit.String}) """ =>
        patch += ctx.removeTokens(t.tokens)
      case t @ q""" buildInfoPackage := "fix" """ =>
        patch += ctx.removeTokens(t.tokens)
        val comma =
          ctx.tokenList.trailing(t.tokens.last).takeWhile(_.is[Token.Comma])
        patch += ctx.removeTokens(comma)
      case q""" "ch.epfl.scala" % "sbt-scalafix" % ${v: Lit.String} """ =>
        patch += ctx.replaceTree(
          v,
          Lit.String(scalafix.Versions.version).syntax
        )
      case t @ q"scalafixSourceroot := sourceDirectory.in(Compile).value" =>
        patch += ctx.replaceTree(
          t,
          """scalacOptions += {
            |    val sourceroot = sourceDirectory.in(Compile).value / "scala"
            |    s"-P:semanticdb:sourceroot:$sourceroot"
            |  }""".stripMargin
        )
      case t @ q"scalaVersion in ThisBuild := V.scala212" =>
        patch += ctx.replaceTree(
          t,
          """inThisBuild(
            |  List(
            |    scalaVersion := V.scala212,
            |    addCompilerPlugin(scalafixSemanticdb),
            |    scalacOptions += "-Yrangepos"
            |  )
            |)""".stripMargin
        )
      case Importer(ref, importees) =>
        val syntax = ref.syntax
        if (syntax.startsWith("scala.meta.contrib") ||
            syntax.startsWith("scalafix.syntax")) {
          // do nothing
        } else if (syntax.startsWith("org.langmeta.") ||
                   syntax.startsWith("scala.meta.")) {
          addImporter(importer"scala.meta._", importees)
        } else if (syntax.startsWith("scalafix.")) {
          addImporter(importer"scalafix.v0._", importees)
        }
      case t @ init"SemanticRuleSuite(..$_)" =>
        patch += ctx.replaceTree(
          t,
          """scalafix.testkit.SemanticRuleSuite""".stripMargin
        )
    }

    patch ++ imports.values.map(ctx.addGlobalImport)
  }

}
