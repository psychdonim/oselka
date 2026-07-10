package org.psyd.oselka
package app.http.apis

import auth._

import sttp.tapir._
import sttp.tapir.json.circe.jsonBody
import sttp.model.StatusCode
import sttp.tapir.generic.auto._
import io.circe.generic.auto._
import cats.Functor
import cats.syntax.functor._
import sttp.model.headers.CookieValueWithMeta
import org.psyd.oselka.auth.tokens.TokensPair
import sttp.model.headers.Cookie
import sttp.tapir.EndpointOutput.OneOf
import sttp.tapir.EndpointOutput.OneOfVariant
import org.psyd.oselka.auth.errors.SignUpError

final class Auth[F[_]: Functor](
  private val authService: AuthService[F]
) {
  private val authRoot = "auth"

  def postSignUp = endpoint
    .post
    .securityIn(authRoot / "signup")
    .in(jsonBody[Creds])
    .out(
      statusCode(StatusCode.Created)
      and setCookie("access_token")
      and setCookie("refresh_token")
    )
    .errorOut(statusCode)
    .serverLogic(creds => {
      authService
        .signUp(creds)
        .map { tokens => (Cookies.accessCookie(tokens.access), Cookies.refreshCookie(tokens.refresh)) }
        .leftMap { case SignUpError.UserAlreadyExists => StatusCode.Conflict }
        .value
    })

  def postSignIn = endpoint
    .post
    .in(authRoot / "signin")
    .in(jsonBody[Creds])
    .out(statusCode(StatusCode.Created) and jsonBody[TokensPair])

  def postRefresh = endpoint
    .post
    .in(authRoot / "refresh")

  val endpoints = List(
      postSignUp
    )
}
