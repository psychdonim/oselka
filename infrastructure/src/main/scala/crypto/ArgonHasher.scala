package org.psyd.oselka
package infrastructure.crypto

import cats.effect.Sync
import de.mkammerer.argon2._

import org.psyd.oselka.auth.Hasher

final class ArgonHasher[F[_]: Sync] extends Hasher[F] {
  private val factory = Argon2Factory.create()

  override def hash(input: String): F[String] = 
    Sync[F] blocking {
      factory.hash(3, 65536, 1, input.toCharArray)
    }

  override def verify(input: String, hash: String): F[Boolean] = 
    Sync[F] blocking {
      factory.verify(hash, input.toCharArray)
    }
}
