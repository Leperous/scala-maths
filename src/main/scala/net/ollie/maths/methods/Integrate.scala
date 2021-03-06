package net.ollie.maths.methods

import net.ollie.maths.expressions.{Expression, Integrable, Univariate}
import net.ollie.maths.Variable
import net.ollie.maths.numbers._
import net.ollie.maths.numbers.constants.Zero

/**
 * Real integrals.
 * Created by Ollie on 19/01/14.
 */
object Integrate {

    def apply(fn: Variable => Univariate, from: Real, to: Real)
            (implicit method: NumericalIntegrationMethod = SimpsonsIntegrationMethod): Real = {
        if (from >= to) Zero
        else method(fn, from, to)
    }

    def apply(integrand: Univariate, from: Real, to: Real)
            (implicit method: NumericalIntegrationMethod): Real = {
        if (from >= to) Zero
        else method(integrand, from, to)
    }

    def apply(integrand: Expression, wrt: Variable, from: Expression, to: Expression): Expression = {
        integrand match {
            case i: Integrable => Integrate(i.asInstanceOf[Integrable], wrt, from, to)
            case _ => new DefiniteIntegralOf(integrand, wrt, from, to)
        }
    }

    def apply(integrand: Integrable, wrt: Variable, from: Expression, to: Expression): Expression = {
        val integral = integrand.integrate(wrt)
        return integral.replace(wrt, to) - integral.replace(wrt, from)
    }

    def apply(fn: Variable => Expression, from: Expression, to: Expression): Expression = {
        val v = Variable.temp
        Integrate(fn(v), v, from, to)
    }

    def apply(integrand: Expression, wrt: Variable): Integral = new IndefiniteIntegralOf(integrand, wrt)

    def apply(fn: Variable => Expression): Integral = {
        val v = Variable("$v")
        Integrate(fn(v), v)
    }

}

sealed trait Integral
        extends Expression {

    def integrand: Expression

    def variable: Variable

    def variables = integrand.variables

    override def toString = s"∫($integrand) d($variable)"

}

trait DefiniteIntegral
        extends Integral {

    def from: Expression

    def to: Expression

    def isEmpty = false //this.evaluate(SinglePrecision) == 0

    override def toString = s"∫($from:$to)($integrand) d($variable)"

}



private class DefiniteIntegralOf(val integrand: Expression, val variable: Variable, val from: Expression, val to: Expression)
        extends DefiniteIntegral {

    def df(x: Variable) = ???

    def toConstant = ???

    def replace(variables: Map[Variable, Expression]) = {
        Integrate(integrand.replace(variables), variable, from.replace(variables), to.replace(variables))
    }

    def unary_-() = Integrate(-integrand, variable, from, to)

}

private class IndefiniteIntegralOf(val integrand: Expression, val variable: Variable)
        extends Integral {

    def df(x: Variable) = {
        if (x == variable) integrand
        else Integrate(integrand.df(x), variable)
    }

    def toConstant = ???

    def replace(variables: Map[Variable, Expression]) = Integrate(integrand.replace(variables), variable)

    def isEmpty = false

    def unary_-() = Expression.negate(this)

}