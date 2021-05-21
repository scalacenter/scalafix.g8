package fix

import org.scalatest.FunSuiteLike
import scalafix.testkit.AbstractSemanticRuleSuite

class OldSemanticRule extends AbstractSemanticRuleSuite with FunSuiteLike {
  runAllTests()
}
class OldSemanticRule2 extends scalafix.testkit.AbstractSemanticRuleSuite with FunSuiteLike {
  runAllTests()
}