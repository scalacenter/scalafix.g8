package fix

import scala.meta._
import scalafix._
import scalafix.testkit._

class $rule;format="Camel"$_Tests
  extends SemanticRewriteSuite(
    SemanticdbIndex.load(Classpath(AbsolutePath(BuildInfo.inputClassdirectory))),
    AbsolutePath(BuildInfo.inputSourceroot),
    Seq(AbsolutePath(BuildInfo.outputSourceroot))
  ) {
  runAllTests()
}
