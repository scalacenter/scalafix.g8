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
  val isSaveExpectField2 = ensureAccessible(classOf[scalafix.testkit.SemanticRuleSuite].getDeclaredField("isSaveExpect"))
  val fake = classOf[Int]
  val fake2 = 1.asInstanceOf[SemanticRuleSuite]
  val fake3 = 1.asInstanceOf[scalafix.testkit.SemanticRuleSuite]

  runAllTests()
}