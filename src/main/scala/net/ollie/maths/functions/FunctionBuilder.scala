package net.ollie.maths.functions

import net.ollie.maths._
import net.ollie.maths.numbers.Real
import net.ollie.maths.numbers.complex.Complex

/**
 * Can convert an expression into another expression, or a number into another number.
 *
 * Created by Ollie on 11/01/14.
 * @see [[BuiltFunction]]
 */
trait FunctionBuilder {

    def apply(n: Number): Number

    def apply(expression: Expression): Expression = expression match {
        case e if e.isEmpty => empty
        case n: Number => apply(n)
        case _ => create(expression)
    }

    protected[this] def create(expr: Expression): Expression

    protected[this] def empty: Expression

}

/**
 *
 */
trait BuiltFunction
        extends Function {

    protected[this] def builder: FunctionBuilder

    protected[this] def at(n: Number) = builder(n)

    protected[this] def apply(x: Expression) = builder(x)

}

/**
 * Builds an odd expression.
 * @see http://mathworld.wolfram.com/OddFunction.html
 */
trait OddBuiltFunction
        extends BuiltFunction {

    override def unary_-() = apply(-of)

}

trait RealFunctionBuilder
        extends FunctionBuilder {

    def apply(n: Number): Number = Real(n) match {
        case Some(re) => apply(re)
        case _ => ???
    }

    def apply(re: Real): Number

}

trait ComplexFunctionBuilder
        extends RealFunctionBuilder {

    type Z <: Number

    override def apply(n: Number): Z = Complex(n) match {
        case Some(z) => z.toReal match {
            case Some(re) => apply(re)
            case _ => apply(z)
        }
        case _ => ???
    }

    def apply(re: Real): Z = apply(Complex(re))

    def apply(z: Complex): Z

}