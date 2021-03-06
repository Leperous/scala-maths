package net.ollie.maths.numbers

import net.ollie.maths.numbers.constants.Zero
import org.scalatest.{FlatSpec, Matchers}

/**
 * Created by Ollie on 08/01/14.
 */
class InfinityTest extends FlatSpec with Matchers {

    behavior of "Infinity"

    it should "have a multiplicative inverse of 0" in {
        Infinity.inverse shouldBe (Zero)
    }

    it should "add to a non-infinite number" in {
        Infinity + 5 shouldBe (Infinity)
    }

    it should "add to +infinity" in {
        Infinity + Infinity shouldBe (Infinity)
    }

    it should "multiply by +infinity" in {
        Infinity * Infinity shouldBe (Infinity)
    }

    it should "multiply by -infinity" in {
        Infinity * -Infinity shouldBe (-Infinity)
    }

    it should "compare to finite numbers" in {
        Infinity > 0 shouldBe true
        Infinity >= 0 shouldBe true
        0 < Infinity shouldBe true
        0 <= Infinity shouldBe true
    }

}
