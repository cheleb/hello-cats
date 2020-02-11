package catstutorial

import org.scalatest.wordspec.AnyWordSpec

import cats.Applicative

object tree {
  sealed abstract class Tree[A] extends Product with Serializable {
    def traverse[F[_]: Applicative, B](f: A => F[B]): F[Tree[B]] = this match {
      case Tree.Empty() => Applicative[F].pure(Tree.Empty())
      case Tree.Branch(v, l, r) =>
        Applicative[F].map3(f(v), l.traverse(f), r.traverse(f))(Tree.Branch(_, _, _))
    }
  }

  object Tree {
    final case class Empty[A]()                                         extends Tree[A]
    final case class Branch[A](value: A, left: Tree[A], right: Tree[A]) extends Tree[A]
  }

  implicit val traverseForList: Traverse[List] = new Traverse[List] {
    def traverse[G[_]: Applicative, A, B](fa: List[A])(f: A => G[B]): G[List[B]] =
      fa.foldRight(Applicative[G].pure(List.empty[B])) { (a, acc) =>
        Applicative[G].map2(f(a), acc)(_ :: _)
      }
  }

// Example implementation for Tree
  implicit val traverseForTree: Traverse[Tree] = new Traverse[Tree] {
    def traverse[G[_]: Applicative, A, B](fa: Tree[A])(f: A => G[B]): G[Tree[B]] =
      fa.traverse(f)
  }

}

trait Traverse[F[_]] {
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
}

// Example implementation for List

class TraverseSpec extends AnyWordSpec {
  "Traverse" should {
    "apply to tree" in {

       import tree._
       import cats.implicits._

       val v = Tree.Branch(1, Tree.Empty(), Tree.Empty()).traverse(i=>List(i))
       println(v)

    }
  }
}
