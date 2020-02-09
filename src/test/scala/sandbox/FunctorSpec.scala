package sandbox

import cats.Functor

import cats.instances.function._
import cats.syntax.functor._
import org.scalatest.wordspec.AnyWordSpec

final case class Box[A](value: A)

sealed trait Tree[+A]

final case class Branch[A](left: Tree[A], right: Tree[A]) extends Tree[A]

final case class Leaf[A](value: A) extends Tree[A]

object Tree {
  def branch[A](left: Tree[A], right: Tree[A]): Tree[A] =
    Branch(left, right)

  def leaf[A](value: A): Tree[A] =
    Leaf(value)
}

trait Printable[A] {
  self =>
  def format(value: A): String

  def contramap[B](func: B => A): Printable[B] =
    (value: B) => self.format(func(value))
}

trait Codec[A] {

  self =>

  def encode(value: A): String
  def decode(value: String): A
  def imap[B](dec: A => B, enc: B => A): Codec[B] = new Codec[B] {

    override def encode(value: B): String = self.encode(enc(value))

    override def decode(value: String): B = dec(self.decode(value))
  }
}

class FunctorSpec extends AnyWordSpec {

  "cats" should {
    "love functor" in {
      val func1 = (a: Int) => a + 1
      val func2 = (a: Int) => a * 2
      val func3 = (a: Int) => s"$a !"
      val func4 = func1.map(func2).map(func3)
      func4(123)
    }

    "box fun" in {
      final case class Box[A](value: A)

      implicit val bf = new Functor[Box] {
        override def map[A, B](fa: Box[A])(f: A => B): Box[B] = Box(f(fa.value))
      }

      val box = Box[Int](123)

      box.map(value => value + 1)

    }

    "tree functor" in {
      implicit val treeFunctor = new Functor[Tree] {
        override def map[A, B](tree: Tree[A])(f: A => B): Tree[B] =
          tree match {
            case Branch(l, r) =>
              Branch(map(l)(f), map(r)(f))
            case Leaf(v) => Leaf(f(v))
          }
      }

      Tree.branch(Tree.leaf(1), Tree.leaf(2)).map(i => i * 2)
    }

    "contramap" in {

      def format[A](value: A)(implicit p: Printable[A]): String =
        p.format(value)

      implicit val stringPrintable: Printable[String] =
        new Printable[String] {
          def format(value: String): String =
            "\"" + value + "\""
        }

      implicit val booleanPrintable: Printable[Boolean] =
        new Printable[Boolean] {
          def format(value: Boolean): String =
            if (value) "yes" else "no"
        }

      implicit val intPrintable: Printable[Int] =
        new Printable[Int] {
          def format(value: Int): String = value.toString

        }

      //      implicit def boxPrintable[A](implicit pa: Printable[A]) = new Printable[Box[A]] {
      //        override def format(value: Box[A]): String = pa.format(value.value)
      //      }

      implicit def boxPrintable[A](implicit pa: Printable[A]) =
        pa.contramap[Box[A]](_.value)

      format(Box(1))
      format(Box(true))
      format(Box("ooo"))
    }

    "imap" in {

      def encode[A](value: A)(implicit c: Codec[A]): String =
        c.encode(value)
      def decode[A](value: String)(implicit c: Codec[A]): A =
        c.decode(value)

      implicit val intCodec = new Codec[Int] {

        override def encode(value: Int): String = value.toString

        override def decode(value: String): Int = value.toInt
      }

      implicit val doubleCodec = new Codec[Double] {
        override def encode(value: Double): String = value.toString

        override def decode(value: String): Double = value.toDouble
      }

      implicit def boxCodec[A](implicit codec: Codec[A]): Codec[Box[A]] =
        codec.imap(Box(_), _.value)

      encode(1)

      decode[Double]("1.9")

      encode(Box(2))

    }
  }

}
