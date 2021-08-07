import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import cats.data.OptionT
import cats.implicits._
val o1 = Some(1)
//val o1 = Option.empty[Int]
val o2 = Some(2)
val o3 = Some(3)

o1.flatMap(i => o2.map(j => i + j))

for {
  i <- o1
  j <- o2
  k <- o3
} yield i + j + k

val someFuture: Future[Option[String]] = Future.successful(Some("Agnes"))
val boolFuture                         = someFuture.map(some => some.map(name => name == "Agnes"))

val optionT    = OptionT(someFuture)
val optionBool = optionT.map(name => name == "Agnes")

for {
  _ <- OptionT(someFuture)
} yield ()
