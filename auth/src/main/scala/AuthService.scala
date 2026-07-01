package org.psyd.oselka
package auth

final class AuthService[F[_]] {
  def signUp(creds: Creds): F[TokensPair] = ???
  def signIn(creds: Creds): F[TokensPair] = ???
  def refresh(token: String): F[TokensPair] = ???
}  
