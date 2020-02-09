sealed trait Query[A]

case object QueryString extends Query[String]
case object QueryBoolean extends Query[Boolean]

case class QueryPath[A](path: String, next: Query[A]) extends Query[A]

def typeMatch[A, B](a: A, b: B)(implicit eq: A=:=B) = ()
val queryString = QueryPath("my", QueryString)
val queryNestedString = QueryPath("my", QueryPath("oh", QueryString))
typeMatch(queryString, queryNestedString)   // compiles


sealed trait QueryF[+F[_], A]

case object QueryStringF extends QueryF[Nothing, String]
case object QueryBooleanF extends QueryF[Nothing, Boolean]

case class QueryPathF[F[_], A](path: String, next: F[A]) extends QueryF[F,A]

// compiles with `-Ypartial-unification` compiler flag
val expression = QueryPathF("oh", QueryPathF("my", QueryStringF))