package net.ollie.maths.numbers

import org.scalatest.{Matchers, FlatSpec}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * Created by Ollie on 12/01/14.
 */
@RunWith(classOf[JUnitRunner])
class ZeroTest extends FlatSpec with Matchers {

    behavior of "Zero"

    it should "be empty" in {
        Zero.isEmpty shouldBe true
    }

    it should "invert" in {
        val i = Zero.inverse
        i.abs shouldBe Infinity
        i + 1 shouldBe i
    }

    "N(0)" should "equal 0" in {
        val i: Int = 0
        Natural(i) shouldBe Zero
        val i2: BigInt = 0
        Natural(i2) shouldBe Zero
    }

}
