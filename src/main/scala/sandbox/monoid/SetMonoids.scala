package sandbox.monoid

object SetMonoids {

  implicit def union[A] = new Monoid[Set[A]] {

    override def empty: Set[A] = Set.empty

    override def combine(a1: Set[A], a2: Set[A]): Set[A] = a1 union a2
  }

}
