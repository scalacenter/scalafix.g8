package fix

import scala.collection.mutable
import scalafix.v1._
import scala.meta._

class Scalafixg8_v0_6 extends SyntacticRule("Scalafixg8_v0_6") {

  val isPatchOp = Set(
    "removeImportee",
    "addGlobalImport",
    "replaceToken",
    "removeTokens",
    "removeTokens",
    "removeToken",
    "replaceTree",
    "addRight",
    "addLeft",
    "addRight",
    "addLeft",
    "lint",
    "removeGlobalImport",
    "addGlobalImport",
    "replaceSymbol",
    "renameSymbol",
    "replaceSymbols",
    "replaceSymbols"
  )

  val isDocOp = Set(
    "tokenList",
    "tree",
    "input",
    "tokens",
    "matchingParens",
    "tokenList",
    "comments"
  )

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
      case Init(rule @ Type.Name("Rule"), _, List(List(Lit.String(_)))) =>
        patch += Patch.replaceTree(rule, "SyntacticRule")
      case init @ Init(
            Type.Name("SemanticRule"),
            _,
            List(List(Term.Name("index"), Lit.String(ruleName)))
          ) =>
        patch += Patch.replaceTree(init, s"""SemanticRule("$ruleName")""")
      case Ctor.Primary(
            _,
            _,
            List(
              List(
                param @ Term.Param(
                  Nil,
                  Term.Name("index"),
                  Some(Type.Name("SemanticdbIndex")),
                  _
                )
              )
            )
          ) =>
        patch += Patch.removeTokens(param.tokens)
      case defn @ Defn.Def(
            _,
            Term.Name("fix"),
            Nil,
            List(
              List(
                Term.Param(
                  Nil,
                  ctx @ Term.Name("ctx"),
                  Some(ruleCtx @ Type.Name("RuleCtx")),
                  None
                )
              )
            ),
            Some(Type.Name("Patch")),
            _
          ) =>
        patch += Patch.replaceTree(ctx, "implicit doc")
        val isSemantic = defn.parent.exists {
          case t: Template =>
            t.inits.exists {
              case Init(Type.Name("SemanticRule"), _, _) => true
              case _                                     => false
            }
          case _ => false
        }
        val prefix = if (isSemantic) "Semantic" else ""
        patch += Patch.replaceTree(ruleCtx, prefix + "Doc")
      case Term.Select(ctx @ Term.Name("ctx"), Term.Name(op)) =>
        if (isPatchOp(op)) {
          patch += Patch.replaceTree(ctx, "Patch")
        } else if (isDocOp(op)) {
          patch += Patch.replaceTree(ctx, "doc")
        }
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
            |      unmanagedSourceDirectories.in(output, Compile).value,
            |    scalafixTestkitInputSourceDirectories :=
            |      unmanagedSourceDirectories.in(input, Compile).value,
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
        if (
          syntax.startsWith("scala.meta.contrib") ||
          syntax.startsWith("scalafix.syntax")
        ) {
          // do nothing
        } else if (
          syntax.startsWith("org.langmeta.") ||
          syntax.startsWith("scala.meta.")
        ) {
          addImporter(importer"scala.meta._", importees)
        } else if (syntax.startsWith("scalafix")) {
          addImporter(importer"scalafix.v1._", importees)
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
