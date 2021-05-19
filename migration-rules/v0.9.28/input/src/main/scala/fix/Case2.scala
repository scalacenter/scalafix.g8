/*
rule = v0_9_28
 */
package fix

import scala.reflect.ensureAccessible
import org.scalatest.ConfigMap
import scalafix.testkit.SemanticRuleSuite

trait BeforeAndAfterAllConfigMapAlt {
  final def beforeAllAlt: Unit = ()
}
class RuleSuite extends SemanticRuleSuite with BeforeAndAfterAllConfigMapAlt {
  val isSaveExpectField = ensureAccessible(classOf[SemanticRuleSuite].getDeclaredField("isSaveExpect"))

  runAllTests()
}