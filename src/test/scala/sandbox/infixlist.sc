val list = List(2,4,5,8,1,2,3)
val sublist = List(1,2,3)

list match {
  case Nil => "Gros null"
  case h1 :: h2 :: tail => s"($h1,$h2)"
}