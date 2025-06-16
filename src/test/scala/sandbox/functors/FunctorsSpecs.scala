package sandbox.functors

import cats.Functor

import org.scalatest.wordspec.AnyWordSpec

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object FunctorsSpecs {
  implicit val treeFunctor: Functor[Tree] = new Functor[Tree] {
    override def map[A, B](fa: Tree[A])(f: A => B): Tree[B] =
      fa match {
        case Branch(l, r) =>
          Branch(map(l)(f), map(r)(f))
        case Leaf(a) =>
          Leaf(f(a))
      }
  }
}

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] = Branch(left, right)

  def leaf[A](value: A): Tree[A] = Leaf(value)
}

class FunctorsSpecs extends AnyWordSpec {

  "Functions" should {
    "oo" in {
      import cats.syntax.functor._
      import FunctorsSpecs._

      Tree.branch(Tree.leaf(10), Tree.leaf(20)).map(_ * 2)

    }
  }

}
