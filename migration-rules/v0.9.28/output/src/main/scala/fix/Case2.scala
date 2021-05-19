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

  runAllTests()
}
