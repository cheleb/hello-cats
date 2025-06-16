package sandbox.monoid

object BooleanMonoids {
  implicit val booleanAndMonoid: Monoid[Boolean] = new Monoid[Boolean] {

    override def empty: Boolean = true

    override def combine(a1: Boolean, a2: Boolean): Boolean = a1 && a2

  }

  implicit val booleanOrMonoid: Monoid[Boolean] = new Monoid[Boolean] {

    override def empty: Boolean = false

    override def combine(a1: Boolean, a2: Boolean): Boolean = a1 || a2

  }

  implicit val booleanXorMonoid: Monoid[Boolean] = new Monoid[Boolean] {

    override def empty: Boolean = false

    override def combine(a1: Boolean, a2: Boolean): Boolean = (a1 && !a2) || (!a1 && a2)
  }

  implicit val booleanXnorMonoid: Monoid[Boolean] = new Monoid[Boolean] {

    override def empty: Boolean = true

    override def combine(a1: Boolean, a2: Boolean): Boolean = (a1 || !a2) && (!a1 || a2)
  }

}
