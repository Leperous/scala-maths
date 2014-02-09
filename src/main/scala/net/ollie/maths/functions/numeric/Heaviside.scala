package net.ollie.maths.functions.numeric

import net.ollie.maths._
import net.ollie.maths.functions.{ExpressionBuilder, UnivariateFunction}
import net.ollie.maths.numbers.{One, Real, Zero}

/**
 * Created by Ollie on 10/01/14.
 */
object Heaviside
        extends UnivariateFunction[Real, Real]
        with ExpressionBuilder {

    def apply(n: Number): Number = n match {
        case re: Real => apply(re)
        case _ => ???
    }

    def apply(f: Real): Real = f match {
        case Zero => empty
        case _ if f.isStrictlyPositive => One
        case _ => Zero
    }

    protected[this] def create(expr: Expression) = new Heaviside(expr)

    override def toString = "Heaviside(?)"

    private final val HALF = One / 2

    protected[this] def empty = HALF

}

class Heaviside(val of: Expression)
        extends AnyRef
        with FunctionBuilder {

    protected[this] def builder = Heaviside

    protected[this] def derivative(x: Expression) = DiracDelta(x)

    def isEmpty = false

}
