package fix

import scala.collection.mutable
import scalafix.v1._
import scala.meta._

class Scalafix06 extends SyntacticRule("Scalafix06") {

  override def fix(implicit doc: Doc): Patch = {
    val imports = mutable.Map.empty[String, Importer]
    var patch = Patch.empty
    def addImporter(importer: Importer, importees: List[Importee]): Unit = {
      imports(importer.syntax) = importer
      importees.foreach { i =>
        patch += Patch.removeImportee(i)
      }
    }

    doc.tree.traverse {
      case q""" $_.enablePlugins(${t @ q"BuildInfoPlugin"})""" =>
        patch += Patch.replaceTree(t, "ScalafixTestkitPlugin")
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
        patch += Patch.replaceTree(
          t,
          """scalafixTestkitOutputSourceDirectories :=
            |      sourceDirectories.in(output, Compile).value,
            |    scalafixTestkitInputSourceDirectories :=
            |      sourceDirectories.in(input, Compile).value,
            |    scalafixTestkitInputClasspath :=
            |      fullClasspath.in(input, Compile).value""".stripMargin
        )
      case t @ q""" addSbtPlugin("com.eed3si9n" % "sbt-buildinfo" % ${_: Lit.String}) """ =>
        patch += Patch.removeTokens(t.tokens)
      case t @ q""" buildInfoPackage := "fix" """ =>
        patch += Patch.removeTokens(t.tokens)
        val comma =
          doc.tokenList.trailing(t.tokens.last).takeWhile(_.is[Token.Comma])
        patch += Patch.removeTokens(comma)
      case q""" "ch.epfl.scala" % "sbt-scalafix" % ${v: Lit.String} """ =>
        patch += Patch.replaceTree(
          v,
          Lit.String(scalafix.Versions.version).syntax
        )
      case t @ q"scalafixSourceroot := sourceDirectory.in(Compile).value" =>
        patch += Patch.replaceTree(
          t,
          """scalacOptions += {
            |    val sourceroot = sourceDirectory.in(Compile).value / "scala"
            |    s"-P:semanticdb:sourceroot:$sourceroot"
            |  }""".stripMargin
        )
      case t @ q"scalaVersion in ThisBuild := V.scala212" =>
        patch += Patch.replaceTree(
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
        patch += Patch.replaceTree(
          t,
          """scalafix.testkit.SemanticRuleSuite""".stripMargin
        )
    }

    patch ++ imports.values.map(Patch.addGlobalImport)
  }

}
