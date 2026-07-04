package org.psyd.oselka
package auth.tokens

import org.psyd.oselka.auth.users.UserId

trait JwtService[F[_]] {
  def makeAccessToken(userId: UserId): F[String]
  def makeRefreshToken(userId: UserId): F[String]
  def verify(token: String): F[JwtPayload]
}
