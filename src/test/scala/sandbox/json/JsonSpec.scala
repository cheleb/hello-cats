package sandbox.json

import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

sealed trait Shape {
  def area: Double
}
case class Circle(radius: Double) extends Shape {
  val area = math.pow(math.Pi, 2) * radius
}

case class Person(name: String, size: Int)

object JsonWriterInstances {
  implicit val stringWriter = new JsonWriter[String] {
    override def write(value: String): Json = JsString(value)
  }
  implicit val personWriter: JsonWriter[Person] =
    (value: Person) =>
      JsObject(
        Map("name" -> JsString(value.name), "size" -> JsNumber(value.size))
      )

  implicit val shapeWriter: JsonWriter[Shape] =
    (shape: Shape) => JsObject(Map("area" -> JsNumber(shape.area)))

  implicit val circleWriter: JsonWriter[Circle] =
    (circle: Circle) => JsObject(Map("radius" -> JsNumber(circle.radius)))
}

object ContravarianceSample {

  def format[A](value: A, writer: JsonWriter[A]) = writer.write(value)

}

class JsonSpec extends AnyWordSpec with Matchers {

  import JsonWriterInstances._
  import JsonSyntax._

  "Person json writer" should {
    "write person as json through interface object" in {
      Json.toJson(Person("Agnes", 160))
    }
    "write person as json through interface syntax" in {
      Person("Agnes", 160).toJson
    }
  }

  "Shape json write" should {
    "write circles as shape" in {
      val circle = Circle(2)
      println(circle.toJson)
    }
  }

  "Format contravariance" should {
    "Only compile if JsonWriter is contravariant" in {
      ContravarianceSample.format[Circle](Circle(160), JsonWriterInstances.shapeWriter)
      ContravarianceSample.format[Circle](Circle(160), JsonWriterInstances.circleWriter)

    }
  }

}
