package sandbox.printable

trait Printable[A] {
  def format(value: A): String
}

object PrintableInstances {
  implicit val stringPrintable = new Printable[String] {
    override def format(value: String): String = value
  }

  implicit val intPrintable = new Printable[Int] {
    override def format(value: Int): String = s"$value"
  }
}

object Printable {

  def format[A](value: A)(implicit printable: Printable[A]): String = printable.format(value)

  def print[A](value: A)(implicit printable: Printable[A]): Unit = println(format(value))
}

object PrintableSyntax {

  implicit class PrintableOps[A](value: A) {
    def format(implicit printable: Printable[A]): String =
      printable.format(value)

    def print(implicit printable: Printable[A]): Unit =
      println(value.format(printable))
  }

}
