package rewrite
import scalafix._, rewrite._, util._, config._, TreePatch._, TokenPatch._
import scala.meta._, semantic._, contrib._
import scala.collection.immutable.Seq

object MyRewrite {
  val seqToList: Rewrite[Mirror] = Rewrite[Mirror] { ctx =>
    import ctx._
    implicit val mirror = ctx.mirror
    val q"${seq: Term.Ref}()" = q"Seq()"
    logger.elem(seq.symbol)
    tree.collect {
      case ref: Term.Name if ref.symbol == seq.symbol =>
        Seq(
          AddGlobalImport(importer"scala.collection.immutable.Vector"),
          Rename(ref, q"Vector")
        )
    }.flatten
  }
  val hoconConfig: String =
    """
      |patches.addGlobalImports = [
      |  "scala.language.higherKinds"
      |  "scalafix._"
      |  "java.{util => ju}"
      |  "scala.collection.JavaConverters._"
      |]
      |""".stripMargin
  val Right(config) = ScalafixConfig.fromString(hoconConfig)
  def main(args: Array[String]): Unit = {
    val mirror = Mirror()
    logger.elem(mirror.database)
    mirror.sources.foreach { source =>
      implicit val ctx = RewriteCtx(source, config, mirror)
      val patches = seqToList.rewrite(ctx)
      val patched = Patch(patches)
      logger.elem(source, patched)
    }
  }
}
