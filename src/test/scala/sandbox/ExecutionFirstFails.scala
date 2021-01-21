package sandbox

import cats.effect._
import scala.concurrent.duration.DurationInt

object ExecutionFirstFails extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    (for {
      _ <- IO.sleep(2 second) *> IO(println(s"prout 0"))
      _ <- IO.sleep(5 second) *> IO.raiseError(new Exception)
      _ <- IO.sleep(5 second) *> IO(println(s"prout 2"))

    } yield ExitCode.Success).handleErrorWith(_ => IO(ExitCode.Error))
}
