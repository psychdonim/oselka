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

final class Auth[F[_]: Functor](
  private val authService: AuthService[F]
) {
  private val authRoot = "auth"

  def postSignIn = endpoint
    .post
    .securityIn(authRoot / "signin")
    .in(jsonBody[Creds])
    .out(
      statusCode(StatusCode.Ok)
      and setCookie("access_token")
      and setCookie("refresh_token")
    )
    .errorOut(statusCode(StatusCode.BadRequest))
    /*.serverLogic(creds => {
      authService.signIn(creds)
        .map(tokens => (
            CookieValueWithMeta(
                "access_token",
                tokens.access,
                httpOnly = true,
                secure = true,
                path = Some("/")
              ),
            CookieValueWithMeta(
                "refresh_token",
                tokens.refresh,
                httpOnly = true,
                secure = true,
                path = Some("/auth/refresh")
              )
          ))
    })*/

  def postSignUp = endpoint
    .post
    .in(authRoot / "signup")
    .in(jsonBody[Creds])
    .out(statusCode(StatusCode.Created) and jsonBody[TokensPair])

  def postRefresh = endpoint
    .post
    .in(authRoot / "refresh")

  val endpoints = List(
      postSignIn,
      postSignUp
    )
}
