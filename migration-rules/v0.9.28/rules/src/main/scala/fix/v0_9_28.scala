package fix

import scalafix.v1._
import scala.meta._

// https://github.com/scalacenter/scalafix/pull/1176

class v0_9_28 extends SemanticRule("v0_9_28") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    val symbolMatcger = SymbolMatcher.exact("scalafix/testkit/SemanticRuleSuite#")
    doc.tree.collect {
      case t @ Importee.Name(Name("SemanticRuleSuite")) =>
        Patch.removeImportee(t)

      case symbolMatcger(tree) if tree.isInstanceOf[Init] =>
        Patch.addGlobalImport(
          importer"scalafix.testkit.AbstractSemanticRuleSuite"
        ) +
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.replaceTree(tree, "AbstractSemanticRuleSuite with FunSuiteLike")

      case t @ q"$classOf[${symbolMatcger(tree)}]" =>
        Patch.replaceTree(tree, s"AbstractSemanticRuleSuite")

      case t @ q"$classOf[SemanticRuleSuite]" =>
        Patch.replaceTree(t, s"$classOf[AbstractSemanticRuleSuite]")

      case t @ q"$classOf[scalafix.testkit.SemanticRuleSuite]" =>
        Patch.replaceTree(t, s"$classOf[AbstractSemanticRuleSuite]")

    }.asPatch
  }
}
