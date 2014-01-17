package net.ollie.maths.numbers

import scala.math.BigDecimal.RoundingMode.RoundingMode

import net.ollie.maths.functions.numeric.GreatestCommonDivisor
import net.ollie.maths.methods.ApproximatelyEvaluated
import net.ollie.maths.DifferentiableFraction

/**
 * A number that can be expressed as an integer divided by another integer.
 * Created by Ollie on 04/01/14.
 * @see http://mathworld.wolfram.com/RationalNumber.html
 */
trait RationalNumber
        extends RealNumber {

    def numerator: IntegerNumber

    def denominator: IntegerNumber

    override def inverse = IntegerFraction(denominator, numerator)

    def isEmpty: Boolean = numerator.isEmpty

    override def approximatelyEvaluate(precision: Precision)(implicit mode: RoundingMode): BigDecimal = numerator.evaluate(precision) / denominator.evaluate(precision)

    //    override def ?+(that: RealNumber) = that match {
    //        case r: RationalNumber => Some(IntegerFraction((numerator * r.numerator) + (r.numerator * denominator), denominator * r.denominator))
    //        case _ => super.?+(that)
    //    }

    override def ?*(that: RealNumber) = that match {
        case r: RationalNumber => Some(IntegerFraction(r.numerator * numerator, r.denominator * denominator))
        case d: RealNumber if d == denominator => Some(numerator)
        case _ => super.?*(that)
    }

    override def ?==(that: RealNumber) = that match {
        case r: RationalNumber => Some(this.numerator == r.numerator && this.denominator == r.denominator)
        case _ => super.?==(that)
    }

    override def toString: String = numerator.toString + "/" + denominator.toString

}

object IntegerFraction {

    def apply(numerator: IntegerNumber, denominator: IntegerNumber): RealNumber = common(numerator, denominator) match {
        case Some(m) => m
        case _ => (numerator, denominator) match {
            case (n1: NaturalNumber, n2: NaturalNumber) => NaturalNumberFraction(n1, n2)
            case _ => reduce(numerator, denominator) match {
                case Some((n, d)) => apply(n, d)
                case _ => new IntegerFraction(numerator, denominator)
            }
        }
    }

    def common(numerator: IntegerNumber, denominator: IntegerNumber): Option[RealNumber] = (numerator, denominator) match {
        case (_, Zero) => Some(numerator * denominator.inverse)
        case (Zero, _) => Some(Zero)
        case (_, One) => Some(numerator)
        case _ if numerator == denominator => Some(One)
        case _ => None
    }

    def reduce(numerator: IntegerNumber, denominator: IntegerNumber): Option[(IntegerNumber, IntegerNumber)] = {
        GreatestCommonDivisor(numerator, denominator) match {
            case One => None
            case gcd => Some(IntegerNumber(numerator.evaluate / gcd.evaluate), IntegerNumber(denominator.evaluate / gcd.evaluate))
        }
    }

}

class IntegerFraction private[numbers](override val numerator: IntegerNumber, override val denominator: IntegerNumber)
        extends DifferentiableFraction(numerator, denominator)
        with RationalNumber
        with ApproximatelyEvaluated {

    require(!denominator.isEmpty)

    override def unary_-() = IntegerFraction(-numerator, denominator)

    override def squared = NaturalNumberFraction(numerator.squared, denominator.squared)

    override def ?*(that: RealNumber) = numerator ?* that match {
        case Some(m: IntegerNumber) => Some(IntegerFraction(m, denominator))
        case _ => super.?*(that)
    }

    override def /(that: RealNumber) = that match {
        case rational: RationalNumber => this * rational.inverse
        case _ => super./(that)
    }

    override def isEmpty = super[DifferentiableFraction].isEmpty

    override def toConstant = super[RationalNumber].toConstant

    override def variables = super[RationalNumber].variables

}
