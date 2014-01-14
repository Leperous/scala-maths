package net.ollie.maths.functions

import net.ollie.maths._
import net.ollie.maths.numbers.RealNumber

/**
 * Created by Ollie on 11/01/14.
 */
trait DifferentiableExpressionBuilder {

    def apply(n: Number): Number

    def apply(expression: Expression): Expression = expression match {
        case e if e.isEmpty => empty
        case n: Number => apply(n)
        case d: Differentiable => apply(d)
        case _ => create(expression)
    }

    protected[this] def create(expr: Expression): Expression

    def apply(diff: Differentiable): Differentiable = diff match {
        case e if diff.isEmpty => empty
        case n: Number => apply(n)
        case _ => create(diff)
    }

    protected[this] def create(diff: Differentiable): Differentiable

    protected[this] def empty: RealNumber

}

trait DifferentiableUnivariateExpressionBuilder
        extends DifferentiableExpressionBuilder {

    def apply(variable: Variable): DifferentiableUnivariate

}