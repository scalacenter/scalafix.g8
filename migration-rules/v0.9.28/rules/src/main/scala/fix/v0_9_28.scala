package fix

import scalafix.v1._
import scala.meta._

// https://github.com/scalacenter/scalafix/pull/1176

class v0_9_28 extends SemanticRule("v0_9_28") {
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t @ Importee.Name(Name("SemanticRuleSuite")) =>
        Patch.removeImportee(t) +
          Patch.addGlobalImport(
            importer"scalafix.testkit.AbstractSemanticRuleSuite"
          )
      case t @ init"SemanticRuleSuite(..$_)" =>
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.replaceTree(t, "AbstractSemanticRuleSuite with FunSuiteLike")

      case t @ init"SemanticRuleSuite" =>
        Patch.addGlobalImport(importer"org.scalatest.FunSuiteLike") +
          Patch.replaceTree(t, "AbstractSemanticRuleSuite with FunSuiteLike")

      case t @ q"classOf[..$tpesnel]" =>
        Patch.replaceTree(t, "classOf[AbstractSemanticRuleSuite]")
    }.asPatch
  }
}
