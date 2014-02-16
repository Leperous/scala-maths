package net.ollie.maths.numbers.constants

import net.ollie.maths.numbers._
import net.ollie.maths.methods.ApproximatelyEvaluated
import net.ollie.maths.functions.angular.ArcTan

/**
 * Created by Ollie on 05/01/14.
 * @see http://mathworld.wolfram.com/Pi.html
 * @see http://mathworld.wolfram.com/MachinsFormula.html
 */
object Pi
        extends PositiveReal {

    private val PI_100 = BigDecimal("3.1415926535897932384626433832795028841971693993751058209749445923078164062862089986280348253421170679")
    private lazy val MACHIN: Real = (16 * ArcTan(IntegerFraction(1, 5))) - (4 * ArcTan(IntegerFraction(1, 239)))

    protected[this] def doEvaluate(precision: Precision) = {
        if (precision.value < 100) precision(PI_100)
        else ??? // MACHIN.evaluate(precision) //TODO this is slow!
    }

    def isEmpty = false

    def /(that: Natural): PiOrLess = new PiOver(that)

    override def toString = "π"

}

trait PiOrLess
        extends PositiveReal

private class PiOver(val d: Natural)
        extends PiOrLess
        with ApproximatelyEvaluated {

    def isEmpty = !Infinite.is(d)

    protected[this] def approx(precision: Precision) = Pi.approximatelyEvaluate(precision) / d.approximatelyEvaluate(precision)

    override def toString = s"$Pi/$d"

}
