package sandbox

import cats.effect._
import cats.implicits._
import scala.concurrent.duration.DurationInt
import scala.util.Try

object Simple extends IOApp {

  def a()             = IO.sleep(1 second) *> IO.fromTry(Try("aa".substring(1)))
  def b()             = IO.fromTry(Try("ab".substring(1)))
  def c(): IO[String] = IO.raiseError(new RuntimeException("c"))

  val prg1 = List(a(), b(), c()).parSequence
    .handleErrorWith {
      case e =>
        println("Boooom2")
        IO.pure(List.empty)
    }
    .map { list =>
      println(list)
    }

  val prg2 = (a(), b(), c())
    .parMapN {
      case (a, b, c) =>
        println(s"a: $a, b: $b, c: $c")
    }
    .handleErrorWith {
      case e => IO.pure(println("Boom"))
    }

  override def run(args: List[String]): IO[ExitCode] =
    prg1
      .map { _ =>
        ExitCode.Success
      }

}
