package net.ollie.maths.numbers.constants

import net.ollie.maths.numbers._
import net.ollie.maths.functions.angular.ArcTan
import net.ollie.maths.methods.ApproximatelyEvaluated

/**
 * Created by Ollie on 05/01/14.
 * @see http://mathworld.wolfram.com/Pi.html
 * @see http://mathworld.wolfram.com/MachinsFormula.html
 */
object Pi
        extends PiOrLess {

    private val PI_100 = BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")
    private lazy val MACHIN: Real = (16 * ArcTan(IntegerFraction(1, 5))) - (4 * ArcTan(IntegerFraction(1, 239)))

    def evaluate(precision: Precision) = {
        if (precision.digits < 100) precision(PI_100)
        else MACHIN.evaluate(precision) //TODO this is slow!
    }

    def /(that: Natural): PiOrLess = that.toInt match {
        case Some(0) => ???
        case Some(1) => Pi
        case Some(2) => HalfPi
        case Some(4) => QuarterPi
        case _ => new PiOver(that)
    }

    override def toString = "π"

}

trait PiOrLess
        extends PositiveNamedReal

class PiOver protected[constants](val over: Natural)
        extends PiOrLess
        with ApproximatelyEvaluated {

    require(!over.isEmpty)

    override def isEmpty = Infinite.is(over)

    def doApproximatelyEvaluate(precision: Precision) = Pi.approximatelyEvaluate(precision) / over.approximatelyEvaluate(precision)

    override def toString = s"$Pi/$over"

}

object HalfPi
        extends PiOver(2)

object QuarterPi
        extends PiOver(4)