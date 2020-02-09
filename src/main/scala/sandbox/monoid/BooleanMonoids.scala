package sandbox.monoid

object BooleanMonoids {
  implicit val booleanAndMonoid = new Monoid[Boolean] {

    override def empty: Boolean = true

    override def combine(a1: Boolean, a2: Boolean): Boolean = a1 && a2

  }

  implicit val booleanOrMonoid = new Monoid[Boolean] {

    override def empty: Boolean = false

    override def combine(a1: Boolean, a2: Boolean): Boolean = a1 || a2

  }

  implicit val booleanXorMonoid = new Monoid[Boolean] {

    override def empty: Boolean = false

    override def combine(a1: Boolean, a2: Boolean): Boolean = (a1 && !a2) || (!a1 && a2)
  }

  implicit val booleanXnorMonoid = new Monoid[Boolean] {

    override def empty: Boolean = true

    override def combine(a1: Boolean, a2: Boolean): Boolean = (a1 || !a2) && (!a1 || a2)
  }

}
