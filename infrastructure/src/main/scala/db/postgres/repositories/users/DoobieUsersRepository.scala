package org.psyd.oselka
package infrastructure.db.postgres.repositories.users

import org.psyd.oselka.auth.users.UsersRepository
import org.typelevel.doobie._
import org.typelevel.doobie.implicits._
import org.typelevel.doobie.util.transactor.Transactor
import org.psyd.oselka.auth.users.User
import cats.effect.Async
import cats.syntax.all._
import org.typelevel.doobie.postgres.implicits._
import org.psyd.oselka.auth.users.UserId
import org.psyd.oselka.auth.users

final class DoobieUsersRepository[F[_]: Async](
    xa: Transactor[F]
  ) extends UsersRepository[F] {
  override def create(user: User): F[Unit] = 
    sql"""
      insert into users (id, name, hash)
      values (${user.id.uuid}, ${user.name}, ${user.hash})
    """
      .update
      .run
      .transact(xa)
      .void

  override def delete(id: UserId): F[Unit] =
    sql"""
      delete from users
      where id = ${id.uuid}
    """
      .update
      .run
      .transact(xa)
      .void

  override def find(id: UserId): F[Option[User]] = 
    sql"""
      select id, name, hash from users
      where id = ${id.uuid}
    """
      .query[UserRow]
      .option
      .transact(xa)
      .map ( _.map ( _.toUser ))

  override def findByName(name: String): F[Option[User]] = 
    sql"""
      select id, name, hash from users
      where name = ${name}
    """
      .query[UserRow]
      .option
      .transact(xa)
      .map ( _.map ( _.toUser ))

  override def update(user: User): F[Unit] = 
    sql"""
      update users
      set name = ${user.name}, hash = ${user.hash}
      where id = ${user.id.uuid}
    """
      .update
      .run
      .transact(xa)
      .void
}
