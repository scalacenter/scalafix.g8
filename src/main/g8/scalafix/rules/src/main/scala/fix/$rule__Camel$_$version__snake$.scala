package fix

import scalafix._
import scala.meta._

case class $rule;format="Camel"$_$version;format="snake"$(index: SemanticdbIndex) extends SemanticRule(index, "$rule;format="Camel"$_$version;format="snake"$") {
  override def fix(ctx: RewriteCtx): Patch = {
    ctx.debugIndex()
    println(s"Tree.syntax: ${ctx.tree.syntax}")
    println(s"Tree.structure: ${ctx.tree.structure}")
    Patch.empty
  }
}
