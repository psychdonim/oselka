package org.psyd.oselka
package app.http.apis

import org.psyd.oselka.auth.tokens.TokensPair
import sttp.model.headers.CookieValueWithMeta
import org.psyd.oselka.auth.tokens.TokenType
import sttp.model.headers.Cookie
import scala.concurrent.duration.DurationInt

object Cookies {
  def accessCookie(token: String): CookieValueWithMeta =
      tokenCookie(value = token, ttlSeconds = 15.minutes.toSeconds, path = "/")

  def refreshCookie(token: String): CookieValueWithMeta = 
      tokenCookie(value = token, ttlSeconds = 15.days.toSeconds, path = "/auth/refresh")

  private def tokenCookie(value: String, ttlSeconds: Long, path: String): CookieValueWithMeta = 
    CookieValueWithMeta(
      value = value,
      expires = None, 
      maxAge = Some(ttlSeconds), 
      domain = None, 
      path = Some(path), 
      secure = true, 
      httpOnly = true, 
      sameSite = Some(Cookie.SameSite.Strict), 
      otherDirectives = Map.empty
    )
}
