package net.ollie.maths.numbers.real


import net.ollie.maths.numbers._

/**
 * Created by Ollie on 18/12/13.
 */
object EulersNumber extends PositiveRealNumber {

    private final val E50 = BigDecimal("2.71828182845904523536028747135266249775724709369995")

    def nthTerm(n: NaturalNumber): PositiveRealNumber = 1 / (n !)

    def isEmpty = false

    protected[this] def eval(precision: Precision) = ???

    override def toString = "e"

}