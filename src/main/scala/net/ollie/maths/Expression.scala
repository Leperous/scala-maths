package net.ollie.maths

import net.ollie.maths.numbers.IntegerNumber

/**
 * Created by Ollie on 01/01/14.
 */
trait Expression {

    def unary_-(): Expression = Expression.negate(this)

    def replace(variable: Variable, expression: Option[Expression]): Expression = expression match {
        case Some(expr) => replace(variable, expr)
        case _ => this
    }

    def replace(variable: Variable, expression: Expression): Expression = replace(Map((variable, expression)))

    def replace(variables: Map[Variable, Expression]): Expression

    def toConstant: Option[Number]

    def variables: Set[Variable]

    def isEmpty: Boolean

    def +(that: Expression): Expression = Series(this, that)

    def -(that: Expression): Expression = this + (-that)

    def *(that: Expression): Expression = Product(this, that)

    def /(that: Expression): Expression = ???

    def ^(that: Expression): Expression = ???

    final override def equals(obj: Any): Boolean = obj match {
        case expr: Expression => this.equals(expr)
        case _ => super.equals(obj)
    }

    def equals(expr: Expression): Boolean = if (this.isEmpty) expr.isEmpty else super.equals(expr)

}

object Expression {

    def negate(expression: Expression) = new NegatedExpression(expression)

    implicit def convert(int: Int): IntegerNumber = IntegerNumber(int)

}

class NegatedExpression(val of: Expression) extends Expression {

    def replace(variables: Map[Variable, Expression]) = -(of.replace(variables))

    def isEmpty = of.isEmpty

    override def toConstant = of.toConstant match {
        case Some(n: Number) => Some(n)
        case _ => None
    }

    def variables = of.variables

    override def toString = "-(" + of + ")"

}

trait Nonvariate
        extends Expression {

    def variables = Set()

}

trait Univariate
        extends Expression {

    def variable: Variable

    def variables = Set(variable)

    def apply[N <: Number](n: N)(implicit conversion: IdentityArithmetic[Number, N#System]): N#System = conversion.convert(replace(variable, n).toConstant).get

}