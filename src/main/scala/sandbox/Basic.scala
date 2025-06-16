package sandbox

import cats.effect._
//import cats.implicits._

import scala.annotation.tailrec

object Basic extends IOApp {
  @tailrec
  def sumIO(n: Long, acc: Long = 0): IO[Long] =
    if (n <= 0)
      IO.pure(acc)
    else
      sumIO(n - 1, acc + n)

  def run(args: List[String]): IO[ExitCode] =
    for {
      res <- sumIO(10)
      _   <- IO(println(s"res is $res"))
    } yield ExitCode.Success
}

object ShortCircuit extends IOApp {

  def io(n: Int): IO[Int] =
    IO.pure(n) <* IO(println(s"n is $n"))

  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO(println("before"))
      // _ <- List(io(1), io(2), io(3))
      _ <- IO(println("after"))
    } yield ExitCode.Success
}
