package net.ollie.maths

import net.ollie.maths.numbers.{Zero, PositiveReal}
import scala.collection.mutable

/**
 * Created by Ollie on 02/01/14.
 */
trait Number
        extends Nonvariate
        with Invertible
        with Differentiable {

    self =>

    type System >: this.type <: Number {type System = self.System}

    final def narrow: System = this

    def unary_-(): System

    def abs: PositiveReal

    def inverse: System

    def ?+(that: Number): Option[Number]

    def +[R <: Number, Combined <: Number](that: R)
                                          (implicit addition: AdditionArithmetic[System, R#System, Combined]): Combined = {
        addition.add(this, that.narrow)
    }

    def -[R <: Number, Combined <: Number](that: R)
                                          (implicit addition: AdditionArithmetic[System, R#System, Combined]): Combined = {
        addition.add(this, -that)
    }

    override def ?*(that: Expression)(leftToRight: Boolean) = that match {
        case n: Number => this.?*(n)(leftToRight)
        case _ => super.?*(that)(leftToRight)
    }

    final def ?*?(that: Number): Option[Number] = this.?*(that)(true) match {
        case Some(n) => Some(n)
        case _ => that.?*(this)(false) match {
            case Some(n) => Some(n)
            case _ => None
        }
    }

    def ?*(that: Number)(leftToRight: Boolean): Option[Number]

    def *[R <: Number, Combined <: Number](that: R)
                                          (implicit multiplication: MultiplicationArithmetic[System, R#System, Combined]): Combined = {
        multiplication.multiply(this, that.narrow)
    }

    /**
     * Exponentiation.
     * @param power
     * @param exponentiation
     * @tparam R power type
     * @tparam Combined result type
     * @return a number
     */
    def ^[R <: Number, Combined <: Number](power: R)
                                          (implicit exponentiation: ExponentiationArithmetic[System, R#System, Combined]): Combined = {
        exponentiation.exponent(this, power.narrow)
    }

    def ?^(that: Number): Option[Number]

    /**
     * Tetration.
     * @param tower
     * @param tetration
     * @tparam R
     * @tparam Combined
     * @return
     * @see http://mathworld.wolfram.com/PowerTower.html
     */
    def ^^[R <: Number, Combined <: Number](tower: R)
                                           (implicit tetration: TetrationArithmetic[System, R#System, Combined]): Combined = {
        tetration.tetrate(this, tower.narrow)
    }

    override def df(x: Variable): EmptyNumber = Zero

    def toConstant: Option[System] = Some(narrow)

    def replace(variables: Map[Variable, Expression]): System = narrow

    final override def equals(expr: Expression) = expr match {
        case n: Number => this.equals(n)
        case _ => expr.toConstant match {
            case Some(n: Number) => this.equals(n)
            case _ => super.equals(expr)
        }
    }

    def equals(n: Number) = super.equals(n)

}

abstract class NumberSeries[N <: Number](val terms: Seq[N])
        extends Number {

    protected[this] def simplify(left: Seq[N], right: Seq[N]): Seq[N] = {
        left.foldLeft(right)((seq, next) => simplify(next, seq))
    }

    //TODO should use foldright because the term is being added to the right
    protected[this] def simplify(term: N, terms: Seq[N]): Seq[N] = {
        var simplified = false
        var current = term
        val series = terms.foldLeft(new mutable.ListBuffer[N]())((seq, next) => tryAdd(next, current) match {
            case Some(m) => {
                simplified = true
                current = m
                seq += current
            }
            case _ => seq :+ next
        })
        if (!simplified) series += current;
        series.toSeq
    }

    protected[this] def tryAdd(left: N, right: N): Option[N]

    def isEmpty = terms.forall(_.isEmpty)

    override def toString = terms.mkString("(", " + ", ")")

}