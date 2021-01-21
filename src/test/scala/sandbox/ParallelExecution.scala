package sandbox

import cats.effect._
import scala.concurrent.duration.DurationInt

object ParallelExecution extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    for {
      fiber  <- (IO.sleep(2 second) *> IO(println(s"prout"))).start
      fiber2 <- (IO.sleep(5 second) *> IO(println(s"proutClone"))).start
      _      <- IO(println(s"prout2"))
      _      <- fiber.join &> fiber2.join
    } yield ExitCode.Success
}
