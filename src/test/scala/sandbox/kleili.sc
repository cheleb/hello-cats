trait Monad[F[_]]:
  def pure[A](a: A):F[A]
  def flatMap[A, B](fa: F[A])(ff: A => F[B]): F[B]

import scala.util.*

given Monad[Try] with
  def pure[A](a: A):Try[A] = Success(a)
  def flatMap[A, B](fa: Try[A])(ff: A => Try[B]): Try[B] = fa.flatMap(ff)


final case class Kleisli[F[_], I, O](value: I => F[O]):
  inline def run(i: I):F[O] = value(i)

  inline def compose[O2](other: Kleisli[F, O, O2])(using F:Monad[F]): Kleisli[F, I, O2] =
    Kleisli(input => F.flatMap(value(input))(other.value))

given[F[_], Env](using F: Monad[F]): Monad[[x] =>> Kleisli[F, Env, x]] with
  def pure[A](a: A):Kleisli[F, Env, A] = 
    Kleisli(_ => F.pure(a))

  def flatMap[A, B](fa: Kleisli[F, Env, A])(ff: A => Kleisli[F, Env, B]): Kleisli[F, Env, B] =
    Kleisli(env => F.flatMap(fa.run(env))(a => ff(a).run(env)))
