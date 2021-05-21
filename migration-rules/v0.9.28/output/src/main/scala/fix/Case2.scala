package fix

import scala.reflect.ensureAccessible
import org.scalatest.ConfigMap
import org.scalatest.FunSuiteLike
import scalafix.testkit.AbstractSemanticRuleSuite

trait BeforeAndAfterAllConfigMapAlt {
  final def beforeAllAlt: Unit = ()
}
class RuleSuite extends AbstractSemanticRuleSuite with FunSuiteLike with BeforeAndAfterAllConfigMapAlt {
  val isSaveExpectField = ensureAccessible(classOf[AbstractSemanticRuleSuite].getDeclaredField("isSaveExpect"))
  val isSaveExpectField2 = ensureAccessible(classOf[AbstractSemanticRuleSuite].getDeclaredField("isSaveExpect"))
  val fake = classOf[Int]
  val fake2 = 1.asInstanceOf[AbstractSemanticRuleSuite]
  val fake3 = 1.asInstanceOf[AbstractSemanticRuleSuite]

  runAllTests()
}
