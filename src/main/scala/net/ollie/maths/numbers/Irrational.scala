package net.ollie.maths.numbers

import net.ollie.maths.numbers.constants.{One, Zero}

trait Irrational
    extends Real {

    override def isEmpty = false

    override def ?*(r: Real): Option[Real] = r match {
        case r: Rational => Some(this * r)
        case _ => super.?*(r)
    }

    def *(i: Rational): Real = IrrationalProduct(this, i)

}

object Irrational {

    def is(r: Real): Boolean = r match {
        case _: Irrational => true
        case m: MaybeIrrational => m.isIrrational
        case _ => false
    }

}

trait MaybeIrrational
    extends Real {

    def isIrrational: Boolean

}

object IrrationalProduct {

    def apply(i: Irrational, r: Rational) = (i, r) match {
        case (_, Zero) => Zero
        case (_, One) => i
        case (p: IrrationalProduct, _) => p * r
        case _ => new IrrationalProduct(Seq(i, r))
    }

    def unapply(p: IrrationalProduct): Option[Seq[Real]] = Some(p.terms)

}

class IrrationalProduct protected(override val terms: Seq[Real])
    extends RealProduct(terms) with Irrational {

    override def *(r: Rational) = r match {
        case Zero => Zero
        case One => this
        case IrrationalProduct(seq) => new IrrationalProduct(simplify(terms ++ seq))
        case _ => new IrrationalProduct(simplify(terms ++ r))
    }

}