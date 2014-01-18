scala-maths
===========

Symbolic maths library.

Extensible numeric operations:

    val re:RealNumber = 5
    val z:ComplexNumber = ComplexNumber(1, 2)
    val q:Quaternion = Quaternion(2, 3, 4, 5)
    re + z + q shouldBe(Quaternion(8, 5, 4, 5))
    val something:MyCustomType = ...?
    re + something shouldBe(...?) //Works iff you have defined how MyCustomType adds to Reals

Build up expressions in a human-readable manner:

    val x = Variable("x")
    val ex = (2 * Ln(x)) + x
    val df = ex.df(x)
    val sin = Sin(df)
    val n = sin.replace(x, Pi).toConstant.get
    n.evaluate(4 decimalPlaces) shouldBe(BigDecimal("0.9978"))
