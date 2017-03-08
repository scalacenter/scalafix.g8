package fix

import scala.meta._
import scalafix.testkit._

class $rewrite;format="Camel"$_Tests
  extends SemanticRewriteSuite(
    Database.load(Classpath(AbsolutePath(BuildInfo.inputClassdirectory))),
    AbsolutePath(BuildInfo.inputSourceroot),
    Seq(AbsolutePath(BuildInfo.outputSourceroot))
  ) {
  runAllTests()
}
