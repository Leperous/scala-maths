package net.ollie.maths.numbers.real

import scala.math.BigDecimal.RoundingMode

import net.ollie.maths.{Empty, Variable}
import net.ollie.maths.numbers._

/**
 * Created by Ollie on 06/01/14.
 */
trait SurrealNumber
        extends RealNumber {

    def nearest: RealNumber

    override def abs = nearest.abs

    override def unary_-(): SurrealNumber = ???

    protected[this] def eval(precision: Precision)(implicit mode: RoundingMode.RoundingMode) = nearest.evaluate(precision)(mode)

    override def df(x: Variable) = EmptyForm

}

trait SurrealForm extends SurrealNumber {

    def left: NumberSet

    def right: NumberSet

    override def unary_-(): SurrealForm = ???

    override def toString = "{ " + left.toString + " | " + right.toString + " }"

}

object EmptyForm
        extends SurrealForm
        with Empty {

    def left = NumberSet()

    def right = NumberSet()

    def nearest = Zero

    override def unary_-() = this

    override def variables = Set()

    override def isEmpty = true

    override def df(x: Variable) = super[SurrealForm].df(x)

    override def toString = "{ | }"

}

object TransfiniteForm
        extends SurrealNumber
        with Infinite {

    override def toString = "ω"

    def nearest = Infinity

    protected[this] override def eval(precision: Precision)(implicit mode: RoundingMode.RoundingMode) = Infinity.evaluate(precision)(mode)

}

object InfinitesimalForm
        extends SurrealNumber {

    def isEmpty = false

    override def toString = "ε"

    def nearest = Zero

}

class RegularForm(val left: NumberSet, val right: NumberSet) extends SurrealForm {

    def isEmpty = left.isEmpty && right.isEmpty

    def nearest = ???

    override def unary_-(): SurrealForm = new RegularForm(-right, -left)

}
