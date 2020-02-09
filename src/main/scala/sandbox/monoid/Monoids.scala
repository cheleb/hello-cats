package sandbox.monoid

trait SemiGroup[A] {

  def combine(a1: A, a2: A): A

}

trait Monoid[A] extends SemiGroup[A] {

  def empty: A

}
