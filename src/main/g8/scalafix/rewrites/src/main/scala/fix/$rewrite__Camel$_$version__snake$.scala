package fix

import scalafix._
import scala.meta._

case class $rewrite;format="Camel"$_$version;format="snake"$(mirror: Mirror) extends SemanticRewrite(mirror) {
  def rewrite(ctx: RewriteCtx): Patch = {
    ctx.reporter.info(ctx.tree.syntax)
    ctx.reporter.info(ctx.tree.structure)
    Patch.empty
  }
}
