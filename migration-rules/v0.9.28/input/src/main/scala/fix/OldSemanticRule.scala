/*
rule = v0_9_28
 */
package fix

import scalafix.testkit.SemanticRuleSuite

class OldSemanticRule extends SemanticRuleSuite() {
  runAllTests()
}
class OldSemanticRule2 extends scalafix.testkit.SemanticRuleSuite {
  runAllTests()
}