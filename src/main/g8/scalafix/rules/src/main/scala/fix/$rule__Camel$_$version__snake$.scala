package fix

import scalafix._
import scala.meta._

case class $rule;format="Camel"$_$version;format="snake"$(index: SemanticdbIndex) extends SemanticRule(index, "$rule;format="Camel"$_$version;format="snake"$") {
  def fix(ctx: RewriteCtx): Patch = {
    ctx.debugIndex()
    ctx.debug(ctx.tree.syntax)
    ctx.debug(ctx.tree.structure)
    Patch.empty
  }
}
