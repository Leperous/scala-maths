package net.ollie.maths.sequences

import net.ollie.maths.numbers.constants.{Half, One, Zero}
import net.ollie.maths.numbers.{Natural, Rational}

/**
 * Created by Ollie on 19/02/14.
 * @see http://mathworld.wolfram.com/BernoulliNumber.html
 */
object BernoulliSequence
        extends CachingSequence {

    type Element = Rational

    //private val calculator = ??? //FIXME

    override def apply(n: Natural): Rational = n match {
        case One => -Half
        case _ if !n.isEven => Zero
        case _ => super.apply(n)
    }

    protected[this] def initial = Map(Zero -> One, One -> -Half)

    protected[this] def create(n: Natural): Rational = n.toInt match {
        //case Some(m) => new SmallBernoulliNumber(m, calculator)
        case _ => ??? //TODO
    }

    override def toString = "BernoulliSequence"

}

//private class SmallBernoulliNumber(val n: Int, val calculator: Bernoulli)
//        extends Rational
//        with ApproximatelyEvaluated {
//
//    private lazy val rational = calculator.at(n)
//
//    def numerator = Integer(rational.numer())
//
//    def denominator = Integer(rational.denom())
//
//    override protected[this] def doApproximatelyEvaluate(precision: Precision) = {
//        numerator.evaluate(precision) / denominator.evaluate(precision)
//    }
//
//    override def toString = s"Bernoulli($n)"
//
//}
