/*
rule = "class:fix.Scalafixg8_v0_6"
 */

package fix

import java.io.File
import org.langmeta.io.AbsolutePath
import scalafix.patch.Patch
import org.langmeta.io.Classpath
import org.langmeta.semanticdb.Database
import scalafix.testkit.SemanticRuleSuite
import scala.meta.contrib._
import scala.meta._
import scalafix.syntax._
import scala.collection.mutable.ListBuffer

object Scalafix_v0_6_Test
    extends SemanticRuleSuite(
      Database.load(Classpath(BuildInfo.inputClassdirectory.toString)),
      AbsolutePath(BuildInfo.inputSourceroot),
      Seq(AbsolutePath(BuildInfo.outputSourceroot))
    ) {
  ListBuffer.empty[Int].append(1)
  Lit.String("1").collectFirst { case q"1" => q"2".parents; Patch.empty }
  // Add code that needs fixing here.
}

object BuildInfo {
  def inputClassdirectory: File = null
  def inputSourceroot: File = null
  def outputSourceroot: File = null
}
