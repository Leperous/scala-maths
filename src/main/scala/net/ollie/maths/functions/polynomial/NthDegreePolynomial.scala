package net.ollie.maths.functions.polynomial

import net.ollie.maths.Variable
import net.ollie.maths.numbers.Natural
import net.ollie.maths.numbers.complex.Complex

/**
 * @see [[LinearPolynomial]]
 * @see [[QuadraticPolynomial]]
 */
trait NthDegreePolynomial
    extends Polynomial {

}

object NthDegreePolynomial {

    def apply(x: Variable, coefficients: Seq[Complex]): UnivariatePolynomial = degree(coefficients) match {
        case 0 => Polynomial(x, coefficients.apply(0))
        case 1 => Polynomial(x, coefficients.apply(1), coefficients.apply(0))
        case 2 => Polynomial(x, coefficients.apply(2), coefficients.apply(1), coefficients.apply(0))
        case _ => new UnivariateNthDegreePolynomial(x, coefficients)
    }

    def degree(coefficients: Seq[Complex]): Int = coefficients.lastIndexWhere(c => !c.isZero)

}

private class UnivariateNthDegreePolynomial(val x: Variable, val coefficients: Seq[Complex])
    extends NthDegreePolynomial with UnivariatePolynomial {

    require(coefficients.length > 3)

    override type Coefficient = Complex

    override val of = x

    override val degree = NthDegreePolynomial.degree(coefficients)

    override lazy val roots = new NthDegreeRoots(this)

    override def coefficient(power: Natural): Complex = {
        if (power < coefficients.length) coefficients.apply(power.requireInt)
        else 0
    }

    class NthDegreeRoots(val of: UnivariateNthDegreePolynomial)
        extends PolynomialRoots[Complex, Complex] {

        override def principal = ???

        override def values = ???

    }

    override def dx: UnivariatePolynomial = {
        val newCoefficients = coefficients.slice(1, coefficients.length).zipWithIndex.map(t => t._1 * (t._2 + 1))
        Polynomial(x, newCoefficients);
    }

}
