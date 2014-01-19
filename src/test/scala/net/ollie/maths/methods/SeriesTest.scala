package net.ollie.maths.methods

import net.ollie.maths.{Expression, Variable}
import net.ollie.maths.numbers.{RealNumber, IntegerNumber, NaturalNumber}
import org.scalatest.{FlatSpec, Matchers}
import net.ollie.maths.numbers.complex.ComplexNumber

/**
 * Created by Ollie on 19/01/14.
 */
class SeriesTest extends FlatSpec with Matchers {

    val x = Variable("x")
    val y = Variable("y")

    behavior of "x + y"

    it should "convert to constant" in {
        val series = Series(Seq(x, y))
        series.toConstant shouldBe (None)
        val r = RealNumber(3)
        val z = ComplexNumber(5, 7)
        series.replace(Map(x -> r, y -> z)).toConstant shouldBe (Some(ComplexNumber(8, 7)))
        series.replace(Map(x -> z, y -> r)).toConstant shouldBe (Some(ComplexNumber(8, 7)))
    }

    it should "not convert to constant" in {
        val series = Series(Seq(x, y))
        val r = RealNumber(3)
        series.replace(x, r).toConstant shouldBe (None)
        series.replace(y, r).toConstant shouldBe (None)
        series.replace(Map(x -> r, y -> r)).toConstant shouldBe (Some(r + r))
    }

    "Sum of n*x from 0 to 5" should "be 15*x" in {
        def f(n: IntegerNumber): Expression = n * x
        val sum = Series(f, 0, 5)
        println(sum)
        sum.isEmpty shouldBe (false)
        sum.variables shouldBe (Set(x))
        sum.df(x).toConstant shouldBe (Some(NaturalNumber(15)))
        sum.replace(x, 1).toConstant shouldBe (Some(NaturalNumber(15)))
        sum.replace(x, 2).toConstant shouldBe (Some(NaturalNumber(30)))
    }

}