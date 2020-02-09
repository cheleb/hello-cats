package sandbox.aux

trait Foo[A] {
  type B

  def value: B
}

object Main extends App {}
