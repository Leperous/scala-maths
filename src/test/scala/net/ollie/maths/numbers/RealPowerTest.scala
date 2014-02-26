package net.ollie.maths.numbers

import org.scalatest.{Matchers, FlatSpec}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import net.ollie.maths.numbers.constants.One
import Precision._
import net.ollie.maths.numbers.complex.Complex
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

/**
 * Created by Ollie on 14/01/14.
 */
@RunWith(classOf[JUnitRunner])
class RealPowerTest extends FlatSpec with Matchers {

    behavior of "1^3"

    it should "equal 1" in {
        val p = RealPower(1, 3)
        p shouldEqual One
    }

    it should "equal 1^4" in {
        val p1 = RealPower(1, 3)
        val p2 = RealPower(1, 4)
        p1 shouldEqual p2
        p2 shouldEqual p1
    }

    behavior of "5 ^ (2/3)"

    {

        val power: RealPower = Integer(5) ^ IntegerFraction(2, 3)

        it should "have 3 values" in {
            power.values.size shouldBe 3
        }

        it should "have real principal" in {
            power.principal.toReal.isDefined shouldBe true
            val principal: Real = power.principal.toReal.get
            principal.evaluate(4 dp) shouldBe BigDecimal("2.9240")
        }

        it should "have principal in values" in {
            power.values.contains(power.principal) shouldBe true
        }

        it should "have complex conjugate non-principals" in {
            val complexValues: Seq[Complex] = new ArrayBuffer[Complex](3) ++ power.values -= power.principal
            val z1 = complexValues(0);
            val z2 = complexValues(1);
            z1.conjugate shouldBe z2;
            z1.re.evaluate(4 dp) shouldBe BigDecimal("-1.4620")
            z1.im.abs.evaluate(4 dp) shouldBe BigDecimal("2.5323")
        }

    }

}
