package org.psyd.oselka
package auth.users

trait UsersRepository[F[_]] {
  def create(user: User): F[Unit]
  def find(id: UserId): F[Option[User]]
  def findByName(name: String): F[Option[User]]
  def update(user: User): F[Unit]
  def delete(id: UserId): F[Unit]
}
