package net.ollie.maths

import net.ollie.maths.numbers.{One, Zero}

/**
 * Created by Ollie on 02/01/14.
 */
object Variable {

    def apply(name: String): Variable = new Variable(name)

}

class Variable(val name: String)
        extends AnyRef
        with Differentiable {

    def isEmpty = false

    def variables = Set(this)

    def toConstant = None

    def replace(variables: Map[Variable, Expression]) = variables.get(this) match {
        case Some(expr) => expr
        case _ => this
    }

    def df(x: Variable) = if (this == x) One else Zero

    override def toString = name

    override def equals(expression: Expression) = expression match {
        case variable: Variable => this.name == variable.name
        case _ => super.equals(expression)
    }

}
