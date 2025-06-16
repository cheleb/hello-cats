package sandbox

import cats.instances.int._
import cats.instances.string._
import cats.instances.option._
import cats.kernel.Eq
import cats.syntax.eq._

object EqSpec extends App {

  /** Type safely check equality
    */
  123 === 123

  (Some(123): Option[Int]) === (Some(123): Option[Int])

  implicit val eqCat: Eq[Cat] = Eq.instance[Cat] { (a, b) =>
    a.name === b.name &&
    a.age === b.age &&
    a.color === b.color
  }

  val cat1 = Option(Cat("acat", 1, "white"))
  val cat2 = Some(Cat("acat", 1, "white"))

  println(cat1 === cat2)

}
