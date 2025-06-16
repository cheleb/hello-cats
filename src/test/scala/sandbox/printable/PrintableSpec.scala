package sandbox.printable

import sandbox.Cat
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should.Matchers

class PrintableSpec extends AnyWordSpec with Matchers {

  val felixTheCat = "felix is a 2 year black/white cat.";

  val felix = Cat("felix", 2, "black/white")

  "Printable" should {

    implicit val catPrintable: Printable[Cat] = new Printable[Cat] {
      override def format(value: Cat): String = {

        import PrintableInstances._

        val name  = Printable.format(value.name)
        val age   = Printable.format(value.age)
        val color = Printable.format(value.color)

        s"$name is a $age year $color cat."
      }
    }

    "format cat" in {
      Printable.format(felix) shouldEqual felixTheCat
    }

    "print a cat" in {
      Printable.print(felix)
    }

    "format cat using syntax" in {
      import PrintableSyntax._
      felix.format shouldEqual felixTheCat
    }

    "print a cat using syntax" in {
      import PrintableSyntax._
      felix.print
    }

  }

}
