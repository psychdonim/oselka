package org.psyd.oselka
package infrastructure.jwt

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

import cats.effect.Sync

import org.psyd.oselka.auth.tokens.JwtService
import org.psyd.oselka.auth.users.UserId
import org.psyd.oselka.auth.users
import org.psyd.oselka.auth.tokens.JwtPayload
import java.time.Instant
import java.time.temporal.ChronoUnit
import org.psyd.oselka.auth.tokens.TokenType

final class Auth0JwtService[F[_]: Sync](secret: String) extends JwtService[F] {
  private val alg = Algorithm.HMAC256(secret)
  private val verifier = JWT.require(alg).build

  override def makeAccessToken(userId: UserId): F[String] = 
    Sync[F] delay {
      JWT
        .create()
        .withSubject(userId.uuid.toString)
        .withClaim("type", "access")
        .withExpiresAt(Instant.now.plus(15, ChronoUnit.MINUTES))
        .sign(alg)
    }

  override def makeRefreshToken(userId: UserId): F[String] =
    Sync[F] delay {
      JWT
        .create()
        .withSubject(userId.uuid.toString)
        .withClaim("type", "refresh")
        .withExpiresAt(Instant.now.plus(15, ChronoUnit.DAYS))
        .sign(alg)
    }

  override def verify(token: String): F[JwtPayload] = 
    Sync[F] delay {
      val jwt = verifier.verify(token)
      
      val userId = UserId(jwt.getSubject)
      val typeStr = jwt.getClaim("type").asString()
      val tokenType = TokenType.valueOf(typeStr)  

      JwtPayload(
          userId,
          tokenType
        )
    }
}
