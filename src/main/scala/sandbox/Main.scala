package sandbox

//import cats.instances.string._
//import cats.syntax.semigroup._
import cats.effect._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    def loop(ref: Ref[IO, Int]): IO[Boolean] =
      for {
        n        <- ref.get
        _        <- IO(println(s"n is $n"))
        _        <- ref.set(n + 1)
        continue <- IO.pure(n < 10)
        _        <- Stream
      } yield continue

    for {
      ref <- Ref.of[IO, Int](0)
      _   <- IO(println("Hello, world!"))
      _   <- loop(ref).iterateWhile(identity)
    } yield ExitCode.Success
  }
}
