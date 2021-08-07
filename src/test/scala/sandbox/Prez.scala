package sandbox

import cats.effect.IOApp
import cats.effect.{ ExitCode, IO }
import scala.concurrent.duration.DurationInt
object Prez extends IOApp {

  def f(x: Int) = x + 1
  def g(x: Int) = x * 10

  val program = for {
    a     <- IO(f(1))
    fiber <- (IO.sleep(1.second) *> IO(println("coucou"))).start
    b     <- IO(g(a))
    _     <- IO(println(s"The real $b"))
    _     <- fiber.join
  } yield b

  def run(args: List[String]): IO[ExitCode] =
    program
      .map { _ =>
        ExitCode.Success
      }

}
