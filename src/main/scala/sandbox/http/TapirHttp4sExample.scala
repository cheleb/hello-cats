package sandbox.http

import cats.effect._
import org.http4s.HttpRoutes
import org.http4s.server.Router
import org.http4s.ember.server.EmberServerBuilder
import com.comcast.ip4s._
import sttp.tapir._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import io.circe.generic.auto._
import cats.syntax.all._
import org.typelevel.otel4s.context.LocalProvider
import org.typelevel.otel4s.oteljava.context.Context
import org.typelevel.otel4s.oteljava.context.IOLocalContextStorage
import org.typelevel.otel4s.oteljava.OtelJava
import org.typelevel.otel4s.trace.Tracer
import org.typelevel.otel4s.Attribute

import org.typelevel.log4cats.slf4j.Slf4jLogger
import org.typelevel.log4cats.Logger

object TapirHttp4sExample extends IOApp {

  given Logger[IO] = Slf4jLogger.getLogger[IO]

  // Define data models
  case class HelloRequest(name: String)
  case class HelloResponse(message: String)

  // Define the endpoint using Tapir
  val helloEndpoint = endpoint.get
    .in("hello")
    .out(jsonBody[HelloResponse])
    .description("Says hello to the person in the request")

  // Implement the business logic
  def helloLogic(request: Unit): IO[HelloResponse] = IO.pure(HelloResponse(s"Hello, World!"))

  // Convert the endpoint to http4s routes
  def helloRoutes(using tracer: Tracer[IO], logger: Logger[IO]): HttpRoutes[IO] =
    Http4sServerInterpreter[IO]()
      .toRoutes(
        helloEndpoint.serverLogicSuccess: req =>
          tracer.span("hello-endpoint", Attribute("zozo", "bo")).use { implicit span =>
            // This is where you can add tracing logic
            logger.info(s"Received request: $req") >>
            helloLogic(())
          }
      )

  // Add Swagger UI
  val swaggerEndpoint = sttp.tapir.swagger.bundle
    .SwaggerInterpreter()
    .fromEndpoints[IO](List(helloEndpoint), "Hello API", "1.0")

  val swaggerRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(swaggerEndpoint)

  // Combine all routes
  def allRoutes(using tracer: Tracer[IO]) =
    Router(
      "/" -> (helloRoutes <+> swaggerRoutes)
    )

  def program(using tracer: Tracer[IO]) =
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8888")
      .withHttpApp(allRoutes.orNotFound)
      .build
      .use(_ => IO.println("Server started at http://localhost:8080") >> IO.never)
      .as(ExitCode.Success)

  override def run(args: List[String]): IO[ExitCode] =
    implicit val provider: LocalProvider[IO, Context] =
      IOLocalContextStorage.localProvider[IO]

    OtelJava.autoConfigured[IO]().use { otelJava =>
      otelJava.tracerProvider.tracer("com.service").get.flatMap { tracer =>
        program(using tracer)
      }
    }
}
