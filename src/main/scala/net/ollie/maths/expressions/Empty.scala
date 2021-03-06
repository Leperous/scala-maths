package net.ollie.maths.expressions

import net.ollie.maths.numbers.constants.Zero
import net.ollie.maths.{Constant, Variable}

/**
 * An empty expression.
 *
 * For a number this means that it is known to be equal to zero at compile-time.
 * It may not be possible to know if a number is zero at compile-time;
 * the method isEmpty should be called, rather than pattern-matching on this trait.
 *
 * For an expression, it means that it evaluates to zero everywhere.
 *
 * Created by Ollie on 02/01/14.
 * @see Zero
 */
trait Empty
        extends Expression {

    def isEmpty = true

    def variables = Set.empty

    def toConstant: Option[Constant with Empty] = Some(Zero)

    override def unary_-(): Empty = Zero

    override def ?+(that: Expression)(leftToRight: Boolean): Option[Expression] = Some(that)

    override def ?*(that: Expression)(leftToRight: Boolean): Option[Expression] = Some(Zero)

    override def ?/(that: Expression): Option[Expression] = Some(Zero)

    override def df(x: Variable): Empty

    override def hashCode = 0

    override def toString = "0"

}


