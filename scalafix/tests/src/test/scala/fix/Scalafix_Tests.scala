package fix

import scalafix.testkit._

class Scalafix_Tests
    extends SemanticRuleSuite(
      BuildInfo.inputClassdirectory,
      BuildInfo.inputSourceroot,
      Seq(BuildInfo.outputSourceroot)
    ) {
  runAllTests()
}
