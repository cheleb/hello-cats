package catstutorial

import cats.Monoid

import org.scalatest.wordspec.AnyWordSpec

class MonoidSpec extends AnyWordSpec {

  "String sample" should {
    import cats.instances.string._
    "Illustrate basics" in {

      println(Monoid[String].combine("Hi", "There"))
    }
  }

  "Int sample" should {
    import cats.instances.option._
    import cats.instances.int._
    import cats.syntax.semigroup._
    "Illustrate basics" in {

      println(Monoid[Option[Int]].combine(Some(1), Some(2)))
    }

    def add(items: List[Int]): Int  = items.fold(Monoid[Int].empty)(Monoid[Int].combine)
    def add1(items: List[Int]): Int = items.fold(Monoid[Int].empty)(_ |+| _)
    def add0(items: List[Int]): Int = items.fold(0)(_ + _)

    "Fold int list" in {
      print("Folded: " + add(List(1, 2, 3)))
      print(", " + add0(List(1, 2, 3)))
      println(", " + add1(List(1, 2, 3)))
    }
  }

}
