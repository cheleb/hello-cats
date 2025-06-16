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

object TapirHttp4sExample extends IOApp {

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
  val helloRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(
      helloEndpoint.serverLogicSuccess(req => helloLogic(req))
    )

  // Add Swagger UI
  val swaggerEndpoint = sttp.tapir.swagger.bundle
    .SwaggerInterpreter()
    .fromEndpoints[IO](List(helloEndpoint), "Hello API", "1.0")

  val swaggerRoutes: HttpRoutes[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(swaggerEndpoint)

  // Combine all routes
  val allRoutes = Router(
    "/" -> (helloRoutes <+> swaggerRoutes)
  )

  override def run(args: List[String]): IO[ExitCode] =
    // Start the server
    EmberServerBuilder
      .default[IO]
      .withHost(ipv4"0.0.0.0")
      .withPort(port"8080")
      .withHttpApp(allRoutes.orNotFound)
      .build
      .use(_ => IO.println("Server started at http://localhost:8080") >> IO.never)
      .as(ExitCode.Success)
}
