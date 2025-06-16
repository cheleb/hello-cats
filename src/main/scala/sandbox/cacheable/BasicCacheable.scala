package sandbox.cacheable

import cats.effect._
object BasicCacheable extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {

      ref <- Ref.ofEffect(IO.pure(1))

      _ <- ref.update(_ + 1)

      value <- ref.get

      _ <- IO.pure(println("ref: " + value))

    } yield ExitCode.Success

}
