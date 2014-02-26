package net.ollie.maths.functions.numeric

import net.ollie.maths._
import net.ollie.maths.functions.{UnivariateFunction, ComplexFunctionBuilder}
import net.ollie.maths.numbers._
import net.ollie.maths.numbers.constants.{Zero, One, EulersNumber}
import net.ollie.maths.numbers.complex.{PolarComplex, Complex}
import net.ollie.maths.functions.angular.Angle
import net.ollie.maths.methods.MaclaurinSeries

/**
 * Created by Ollie on 18/01/14.
 * @see http://mathworld.wolfram.com/ExponentialFunction.html
 * @see Ln
 */
object Exp
        extends ComplexFunctionBuilder
        with UnivariateFunction[Complex, Complex] {

    type Z = Number

    override def apply(re: Real): Real = re match {
        case Zero => empty
        case MinusInfinity => Zero
        case One => EulersNumber
        case _ => new RealExp(re)
    }

    def apply(z: Complex): Complex = {
        if (z.isEmpty) empty
        else new ComplexExp(z)
    }

    def unapply(exp: ExpOf): Option[Expression] = Some(exp.of)

    protected[this] def create(expr: Expression) = expr match {
        case Ln(of) => of
        case _ => new ExpOf(expr)
    }

    protected[this] def empty = One

}

trait Exp
        extends Expression {

    def of: Expression

    override def toString = s"Exp($of)"

}

class ExpOf(val of: Expression)
        extends Exp
        with Function
        with Invertible {

    def isEmpty = false

    protected[this] def at(n: Number) = Exp(n)

    protected[this] def apply(at: Expression) = Exp(at)

    def inverse = Ln(of)

    protected[this] def derivative(at: Expression) = Exp(at)

}

class RealExp(val of: Real)
        extends PrincipalPositiveRealPower(EulersNumber, of)
        with Exp {

    require(MinusInfinity != of)

    private lazy val series = MaclaurinSeries(Exp, of)

    override protected[this] def doEvaluate(precision: Precision) = series.evaluate(precision)

}

class ComplexExp(val of: Complex)
        extends Complex
        with Exp {

    import Angle._

    private lazy val p = PolarComplex(Exp(of.re), of.im radians)

    def re = p.re

    def im = p.im

    override def toString = super[Complex].toString

}