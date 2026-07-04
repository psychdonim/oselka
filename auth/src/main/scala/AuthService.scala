package org.psyd.oselka
package auth

import tokens._
import users._

import cats.Monad
import cats.syntax.all._
import cats.data.EitherT

import org.psyd.oselka.auth.errors.SignUpError

final class AuthService[F[_]: Monad](
    users: UsersRepository[F],
    hasher: Hasher[F],
    jwt: JwtService[F]
  ) {
  def signUp(creds: Creds): EitherT[F, SignUpError, TokensPair] =
    for {
      _ <- ensureUserNotExists(creds.login)

      hashedPass <- EitherT.liftF(hasher.hash(creds.password))
      newUser = User(UserId.random, creds.login, hashedPass)
      _ <- EitherT.liftF(users.create(newUser))

      access <- EitherT.liftF(jwt.makeAccessToken(newUser.id))
      refresh <- EitherT.liftF(jwt.makeRefreshToken(newUser.id))
    } yield TokensPair(access, refresh)

  private def ensureUserNotExists(name: String) = EitherT {
    users.findByName(name) map {
      case Some(_) => Left(SignUpError.UserAlreadyExists)
      case None => Right(())
    }
  }

  def signIn(creds: Creds): F[TokensPair] = ???
  def refresh(token: String): F[TokensPair] = ???
}  
