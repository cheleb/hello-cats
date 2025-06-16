package sandbox

import cats.effect._

import java.io.File
import java.io.PrintWriter

object BracketSample extends IOApp {

  private def fileBracket() =
    IO(new File("/tmp/zozo.txt") /*.createTempFile("test", "txt")*/ ).bracket { file =>
      IO(new PrintWriter(file)).bracket { writer =>
        IO {
          println("#Write")
          writer.append("Hello")
        }
      } { case w =>
        IO {
          println("#Closing")
          w.close
        }
      }
    } { f =>
      IO {
        println("#Deleting")
        f.delete()
      }
    }

  override def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- fileBracket()
    } yield ExitCode.Success
}
