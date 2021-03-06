package net.ollie.maths.functions.polynomial

import net.ollie.maths._
import net.ollie.maths.expressions.Expression
import net.ollie.maths.numbers._
import net.ollie.maths.numbers.constants.{One, Zero}

/**
 * Created by Ollie on 08/01/14.
 */
trait LegendrePolynomial
    extends AssociatedLegendrePolynomial {

    override type Coefficient = Rational

    override def order = Zero

    override def coefficient(power: Natural): Rational = ??? //TODO

    override def isEmpty = false

    override def equals(expr: Expression) = expr match {
        case p: LegendrePolynomial => degree == p.degree && of == p.of
        case _ => super.equals(expr)
    }

    override def toString = s"LegendreP($degree)($of)"

}

object LegendrePolynomial {

    def apply(l: Natural)(z: Expression): LegendrePolynomial = l match {
        case Zero => new ZeroLegendrePolynomial(z)
        case One => new OneLegendrePolynomial(z)
        case _ => new SomeLegendrePolynomial(l)(z)
    }

    def apply(l: Natural, re: Real): Real = {
        LegendrePolynomial(l)(re).toConstant match {
            case Some(re: Real) => re
            case _ => ???
        }
    }

}

class ZeroLegendrePolynomial(val of: Expression)
    extends ConstantPolynomial(One)
        with LegendrePolynomial {

    //could mixin Natural, but this makes negation ugly.

    override def degree = Zero

}

class OneLegendrePolynomial(val of: Expression)
    extends LegendrePolynomial {

    override def degree = One

    override def coefficient(power: Natural) = Zero

    override def representation = of

}

class SomeLegendrePolynomial(override val degree: Natural)(val of: Expression)
    extends LegendrePolynomial {

    require(degree > One)

    override def representation = {
        ((((2 * degree) - 1) * of * LegendrePolynomial(degree - 1)(of)) - ((degree - 1) * LegendrePolynomial(degree - 2)(of))) / degree
    }

    override def df(x: Variable) = {
        degree * (x * LegendrePolynomial(degree)(x) - LegendrePolynomial(degree - 1)(x)) / ((x ^ 2) + 1)
    }

}