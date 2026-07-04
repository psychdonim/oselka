package org.psyd.oselka
package auth

trait Hasher[F[_]] {
  def hash(input: String): F[String]
  def verify(input: String, hash: String): F[Boolean] 
}
