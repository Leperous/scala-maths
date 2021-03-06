package net.ollie.maths.functions.angular

import ch.obermuhlner.math.big.BigDecimalMath
import net.ollie.maths.expressions.Expression
import net.ollie.maths.functions.numeric.{Signum, SquareRoots}
import net.ollie.maths.functions.{FunctionBuilder, OddBuiltFunction, UnivariateFunction}
import net.ollie.maths.numbers.constants.{One, Zero}
import net.ollie.maths.numbers.{Precision, Real}
import net.ollie.maths.{CachedEvaluated, Constant, Operation}
import net.ollie.utils.BigDecimals

/**
 * Created by Ollie on 10/02/14.
 */
object ArcSin
    extends FunctionBuilder
        with UnivariateFunction[Real, Angle] {

    import Angle._

    def apply(n: Constant) = n match {
        case re: Real => apply(re)
        case _ => ???
    }

    //def apply(d: BigDecimal): Angle = apply(Real(d))

    def apply(re: Real): Angle = re match {
        case _ if re.abs < One => new RealArcSin(re) radians
        case _ if re.abs == One => Signum(re) * One radians
        case _ => Operation.undefined
    }

    protected[this] def create(expr: Expression) = new ArcSinOf(expr)

}

trait ArcSin
    extends Expression {

    def of: Expression

    def isEmpty = of.isEmpty

    override def toString = s"ArcSin($of)"

}

class ArcSinOf(val of: Expression)
    extends OddBuiltFunction
        with ArcSin {

    protected[this] def builder = ArcSin

    protected[this] def derivative(x: Expression) = 1 / SquareRoots(1 - (x ^ 2))

}

class RealArcSin(val of: Real)
    extends Real
        with ArcSin
        with CachedEvaluated {

    protected[this] def doEvaluate(precision: Precision) = {
        BigDecimals.asin(of.evaluate(precision), precision)
    }

}
