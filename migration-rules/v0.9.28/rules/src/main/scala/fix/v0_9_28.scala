package fix

import scalafix.v1._
import scala.meta._

// https://github.com/scalacenter/scalafix/pull/1176

class v0_9_28 extends SemanticRule("v0_9_28") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    val s1 = SymbolMatcher.exact("scalafix/testkit/SemanticRuleSuite#")
    doc.tree.collect {
      case t @ Importee.Name(Name("SemanticRuleSuite")) =>
        Patch.removeImportee(t)

      case s1(tree) if tree.isInstanceOf[Init] =>
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.addGlobalImport(
            importer"scalafix.testkit.AbstractSemanticRuleSuite"
          ) +
          Patch.replaceTree(tree, "AbstractSemanticRuleSuite with FunSuiteLike")

      case t @ init"scalafix.testkit.SemanticRuleSuite" =>
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.replaceTree(t, "scalafix.testkit.AbstractSemanticRuleSuite with FunSuiteLike")

      case t @ init"scalafix.testkit.SemanticRuleSuite()" =>
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.replaceTree(t, "scalafix.testkit.AbstractSemanticRuleSuite with FunSuiteLike")
    }.asPatch
  }
}
