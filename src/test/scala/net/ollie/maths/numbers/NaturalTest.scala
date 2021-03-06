package net.ollie.maths.numbers

import net.ollie.maths.numbers.Precision._
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by Ollie on 12/01/14.
 */
class NaturalTest extends FlatSpec with Matchers {

    behavior of "11!"

    {

        val n = Natural(11) !

        it should "invert" in {
            val i = n.inverse
            i.evaluate(12 dp).toString shouldBe ("2.5052E-8") //TODO this is 5dp!
        }

        it should "negate" in {
            val i = -n
            i.evaluate shouldBe (BigInt(-39916800))
            i.approximatelyEvaluate(4 dp) shouldBe (BigDecimal("-39916800"))
        }

        it should "invert and negate" in {
            val i = n.inverse
            (-i).evaluate(12 dp) shouldBe (BigDecimal("-2.5052E-8"))
        }

        it should "invert and negate and invert and negate" in {
            val i = -(-n.inverse).inverse
            i shouldBe (n)
        }

    }

    "8/4" should "reduce" in {
        val fraction = Natural(8) / Natural(4)
        fraction shouldBe Natural(2)
    }

}
