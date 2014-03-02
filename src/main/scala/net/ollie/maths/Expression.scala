package net.ollie.maths

import net.ollie.maths.functions.numeric.Ln
import net.ollie.maths.methods.{Product, Series}
import net.ollie.maths.numbers.Integer
import net.ollie.maths.numbers.constants.{One, Zero}

/**
 * Created by Ollie on 01/01/14.
 */
trait Expression
        extends Differentiable {

    def unary_-(): Expression

    def replace(variable: Variable, expression: Option[Expression]): Expression = expression match {
        case Some(expr) => replace(variable, expr)
        case _ => this
    }

    def replace(variable: Variable, expression: Expression): Expression = {
        if (variable == expression) this
        else replace(Map((variable, expression)))
    }

    def replace(variables: Map[Variable, Expression]): Expression

    def toConstant: Option[Constant]

    def variables: Set[Variable]

    def isEmpty: Boolean

    final def +(that: Expression): Expression = this ?+? that match {
        case Some(expression) => expression
        case _ => Expression.series(this, that)
    }

    def -(that: Expression): Expression = this + (-that)

    final def ?+?(that: Expression): Option[Expression] = this.?+(that)(true) match {
        case Some(x) => Some(x)
        case _ => that.?+(this)(false)
    }

    def ?+(that: Expression)(leftToRight: Boolean): Option[Expression] = None

    /**
     * This times that. Will create an expression product if the multiplication cannot be simplified.
     * @param that
     * @return
     */
    final def *(that: Expression): Expression = this ?*? that match {
        case Some(x) => x
        case _ => Expression.product(this, that)
    }

    /**
     * This try-times-try that.
     * Attempts to simplify to a single term, where possible, by multiplying this by the right term,
     * then the right term multiplied by this on the left.
     * @param that
     * @return
     */
    final def ?*?(that: Expression): Option[Expression] = this.?*(that)(true) match {
        case Some(x) => Some(x)
        case _ => that.?*(this)(false)
    }

    /**
     * This try-times that.
     * @param that
     * @return
     */
    def ?*(that: Expression)(leftToRight: Boolean): Option[Expression] = None

    final def /(that: Expression): Expression = this ?/ that match {
        case Some(x) => x
        case _ => Expression.divide(this, that)
    }

    def ?/(that: Expression): Option[Expression] = {
        if (this equals that) Some(One)
        else None
    }

    def ^(that: Expression): Expression = Expression.power(this, that)

    def df(x: Variable): Expression

    final override def equals(obj: Any): Boolean = obj match {
        case expr: Expression => this.equals(expr)
        case _ => super.equals(obj)
    }

    def equals(expr: Expression): Boolean = {
        if (this.isEmpty) expr.isEmpty
        else super.equals(expr)
    }

}

object Expression {

    def negate(expr: Expression): Expression = if (expr.isEmpty) expr else new NegatedExpression(expr)

    def divide(numerator: Expression, denominator: Expression): Expression = (numerator, denominator) match {
        case _ if numerator.isEmpty => Zero
        case _ => new ExpressionFraction(numerator, denominator)
    }

    def power(base: Expression, power: Expression): Expression = (base, power) match {
        case _ if base.isEmpty => Zero
        case (_, One) => base
        case _ => new ExpressionPower(base, power)
    }

    def series(e1: Expression, e2: Expression): Expression = Series(e1, e2)

    def product(e1: Expression, e2: Expression): Expression = Product(e1, e2)

    implicit def apply(int: Int): Integer = Integer(int)

}

class NegatedExpression(val of: Expression)
        extends Expression {

    def replace(variables: Map[Variable, Expression]) = -(of.replace(variables))

    def isEmpty = of.isEmpty

    def unary_-() = of

    override def toConstant: Option[Constant] = of.toConstant match {
        case Some(n) => Some(-n)
        case _ => None
    }

    def variables = of.variables

    def df(x: Variable) = -(of.df(x))

    override def toString = s"-($of)"

}

class ExpressionFraction(val numerator: Expression, val denominator: Expression)
        extends Expression {

    override def unary_-() = (-numerator) / denominator

    def replace(variables: Map[Variable, Expression]) = numerator.replace(variables) / denominator.replace(variables)

    def toConstant: Option[Constant] = numerator.toConstant match {
        case Some(n: Constant) => denominator.toConstant match {
            case Some(d: Constant) => n ?*? d.inverse
            case _ => None
        }
        case _ => None
    }

    def variables = numerator.variables ++: denominator.variables

    def isEmpty = numerator.isEmpty

    override def df(x: Variable) = (numerator.df(x) / denominator) - (numerator * denominator.df(x) / (denominator ^ 2))

    /**
     * A fraction times something is a fraction.
     * @param that
     * @return
     */
    override def ?*(that: Expression)(leftToRight: Boolean): Option[Expression] = that match {
        case _ if denominator equals that => Some(numerator)
        case ef: ExpressionFraction => Some(this.?*(ef)(leftToRight))
        case _ => super.?*(that)(leftToRight)
    }

    protected def ?*(that: ExpressionFraction)(leftToRight: Boolean): Expression = {
        if (leftToRight) (this.numerator * that.numerator) / (this.denominator * that.denominator)
        else (that.numerator * this.numerator) / (that.denominator * numerator)
    }

    override def toString = s"($numerator/$denominator)"

}

class ExpressionPower(val base: Expression, val power: Expression)
        extends Exponentiated {

    def unary_-() = Expression.negate(this)

    def replace(variables: Map[Variable, Expression]) = base.replace(variables) ^ power.replace(variables)

    def toConstant = base.toConstant match {
        case Some(n) => power.toConstant match {
            case Some(m) => n ?^ m
            case _ => None
        }
        case _ => None
    }

    def variables = base.variables ++: power.variables

    override def ^(x: Expression) = base ^ (power + x)

    override def df(x: Variable) = {
        (base ^ (power - 1)) * ((base.df(x) * power) + (base * Ln(base) * power.df(x)))
    }

}

trait Nonvariate
        extends Expression
        with Integrable {

    def df(x: Variable): Empty = Zero

    def replace(variables: Map[Variable, Expression]): Nonvariate = this

    def variables = Set()

    override protected[this] def integral(x: Variable) = this * x

}

object Univariate {

    implicit def convert(expression: Expression): Univariate = expression match {
        case n: Constant => new NonvariateWrapper(n)
        case u: Univariate => u
        case _ if (expression.variables.size == 1) => new UnivariateWrapper(expression)
        case _ => ???
    }

    private class NonvariateWrapper(val n: Constant)
            extends AnyRef
            with Univariate {

        def unary_-(): Univariate = -n

        override def variables = Set()

        def replace(variables: Map[Variable, Expression]) = this

        def toConstant: Option[Constant] = Some(n)

        def isEmpty = n.isEmpty

        def variable = ??? //TODO

        override def df(x: Variable) = Zero

        override def dx = Zero

        override def ?+(that: Expression)(leftToRight: Boolean) = n.?+(that)(leftToRight)

        override def -(that: Expression) = n - that

        override def ?*(that: Expression)(leftToRight: Boolean): Option[Expression] = {
            n.?*(that)(leftToRight)
        }

        override def ?/(that: Expression) = n ?/ that

        override def equals(that: Expression) = that match {
            case n: Constant => this.n == n
            case _ => super.equals(that)
        }

        override def hashCode = n.hashCode

        override def toString = n.toString

    }

    private class UnivariateWrapper(val expression: Expression)
            extends AnyRef
            with Univariate {

        require(expression.variables.size == 1, "Require 1 variable but " + expression + " had " + expression.variables)

        def unary_-(): Univariate = -expression

        def replace(variables: Map[Variable, Expression]) = expression.replace(variables)

        def toConstant = expression.toConstant

        def isEmpty = expression.isEmpty

        def variable = expression.variables.iterator.next()

        override def ?+(that: Expression)(leftToRight: Boolean) = expression.?+(that)(leftToRight)

        override def ?*(that: Expression)(leftToRight: Boolean) = expression.?*(that)(leftToRight)

        override def ?/(that: Expression) = expression ?/ that

        override def ^(that: Expression) = expression ^ that

        override def df(x: Variable) = expression.df(x)

        override def toString = expression.toString

    }

}

trait Univariate
        extends Expression {

    def unary_-(): Univariate

    def variable: Variable

    def variables = Set(variable)

    def apply[R <: Constant](n: Constant)(implicit conversion: NumberIdentityArithmetic[R]): R = {
        val out: Constant = replace(variable, n).toConstant.get
        conversion(out).get
    }

    def apply(u: Univariate): Univariate = replace(variable, u)

    def df(x: Variable): Univariate

    def dx: Univariate = df(variable)

}
