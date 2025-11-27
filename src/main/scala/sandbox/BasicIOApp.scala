package sandbox

import cats.effect.*
import scala.concurrent.duration.*
import cats.effect.kernel.Resource.ExitCase
import cats.effect.std.Supervisor

object BasicIOApp extends ResourceApp {

  def buildMyAppResource(shutdownSignal: Deferred[IO, Unit]): Resource[IO, Unit] =
    for
      _ <- Resource.make(
        IO(println("Starting my app..."))
      )(_ => IO(println("Shutting down my app...")))

      // Fork off
      _ <- forkBoombox.onError { case e =>
        IO.println(s" 💣 ${e.getMessage}") *> shutdownSignal
          .complete(())
          .void
      }.background
    yield ()

  def forkBoombox =
    for
      _ <- IO.println("Boombox started!")
      _ <- List.fill(10)(IO.sleep(300.millis) *> IO.print(".")).sequence_
      _ <- IO.raiseError[Unit](new RuntimeException("Boombox exploded!"))
    yield ()

  val program = Deferred[IO, Unit].flatMap { shutdown =>
    val app = buildMyAppResource(shutdown)
    IO.race(shutdown.get, app.useForever)
  }.toResource

  override def run(args: List[String]) = program.map(_ => ExitCode.Success)

}
