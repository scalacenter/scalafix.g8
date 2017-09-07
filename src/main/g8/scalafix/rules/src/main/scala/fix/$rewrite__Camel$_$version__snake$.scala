package fix

import scalafix._
import scala.meta._

case class $rewrite;format="Camel"$_$version;format="snake"$(index: SemanticdbIndex) extends SemanticRule(index, "$rewrite;format="Camel"$_$version;format="snake"$") {
  def fix(ctx: RewriteCtx): Patch = {
    ctx.debugIndex()
    ctx.debug(ctx.tree.syntax)
    ctx.debug(ctx.tree.structure)
    Patch.empty
  }
}
