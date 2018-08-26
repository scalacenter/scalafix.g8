package fix

import scalafix.v1._
import scala.meta._

class $rule;format="Camel"$_$version;format="snake"$
    extends SemanticRule("$rule;format="Camel"$_$version;format="snake"$") {

  override def fix(implicit doc: SemanticDoc): Patch = {
    println(s"Tree.syntax: " + doc.tree.syntax)
    println(s"Tree.structure: " + doc.tree.structure)
    Patch.empty
  }

}
