package net.ollie.maths.functions.angular

import scala.Some

import net.ollie.maths._
import net.ollie.maths.functions.{ExpressionBuilder, UnivariateFunction}
import net.ollie.maths.functions.numeric.SquareRoot
import net.ollie.maths.methods.MaclaurinSeries
import net.ollie.maths.numbers.{One, Precision, RealNumber}
import net.ollie.maths.numbers.real.Pi
import org.nevec.rjm.BigDecimalMath

/**
 * Created by Ollie on 03/01/14.
 */
object Cos
        extends ExpressionBuilder {

    def apply(n: Number): Number = n match {
        case re: RealNumber => apply(Radians(re))
        case _ => ???
    }

    def apply(angle: Angle) = if (angle.isEmpty) empty else new RealCos(angle)

    protected[this] def create(expr: Expression): Cos = new Cos(expr)

    protected[angular] def empty = One

}

class Cos(val of: Expression)
        extends CompositeBuilder
        with Invertible {

    protected[this] def builder = Cos

    protected[this] def derivative(x: Expression) = -Sin(x)

    def inverse = ArcCos(of)

    def isEmpty = false

    override def toString = s"Cos($of)"

}

class RealCos(override val of: Angle)
        extends Cos(of)
        with RealNumber {

    private lazy val series = MaclaurinSeries(Cos, of.toRadians)

    protected[this] def eval(precision: Precision) = series.evaluate(precision)

    override def inverse = super[RealNumber].inverse

    override def variables = super[RealNumber].variables

    override def toConstant = Some(this)

}

object Sec
        extends ExpressionBuilder {

    def apply(n: Number) = Cos(n).inverse

    protected[this] def create(expr: Expression) = 1 / Cos(expr)

    protected[this] def empty = Cos.empty.inverse

}

/**
 *
 * @see http://mathworld.wolfram.com/InverseCosine.html
 */
object ArcCos
        extends ExpressionBuilder
        with UnivariateFunction[RealNumber, Angle] {

    import Angle._

    def apply(n: Number) = n match {
        case re: RealNumber => apply(re)
        case _ => ???
    }

    def apply(f: RealNumber): Angle = new RealArcCos(f) radians

    protected[this] def create(x: Expression) = new ArcCos(x)

    protected[this] def empty = Pi / 2

}

class ArcCos(val of: Expression)
        extends CompositeBuilder
        with Invertible {

    protected[this] def builder = ArcCos

    protected[this] def derivative(x: Expression) = -1 / SquareRoot(1 - (x ^ 2))

    def inverse = Cos(of)

    def isEmpty = false

    override def toString = s"ArcCos($of)"

}

class RealArcCos(override val of: RealNumber)
        extends ArcCos(of)
        with RealNumber {

    protected[this] def eval(precision: Precision) = BigDecimalMath.acos(of.approximatelyEvaluate(precision).underlying())

    override def isEmpty = of == One

    override def variables = super[RealNumber].variables

    override def toConstant = super[RealNumber].toConstant

    override def inverse = super[RealNumber].inverse

}