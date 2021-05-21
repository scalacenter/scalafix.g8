/*
rule = v0_9_28
 */
package fix

class OldSemanticRule2 extends scalafix.testkit.SemanticRuleSuite {
  runAllTests()
}
class OldSemanticRule3 extends scalafix.testkit.SemanticRuleSuite() {
  runAllTests()
}