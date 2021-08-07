package sandbox

//import cats.instances.string._
//import cats.syntax.semigroup._
import cats.effect._
import cats.effect.unsafe.implicits.global

object Main extends App {
  //println("Hello " |+| "Cats!")

  val io1 = IO(println("Hello")) *> IO("zozo")

  val str = io1.unsafeRunSync()

  println(str)
}
