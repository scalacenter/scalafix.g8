package fix

import scalafix._
import scala.meta._

case class $rewrite;format="Camel"$_$version;format="snake"$(sctx: SemanticCtx) extends SemanticRewrite(sctx) {
  def rewrite(ctx: RewriteCtx): Patch = {
    ctx.debugSemanticCtx()
    ctx.debug(ctx.tree.syntax)
    ctx.debug(ctx.tree.structure)
    Patch.empty
  }
}
