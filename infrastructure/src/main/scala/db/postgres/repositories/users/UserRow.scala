package org.psyd.oselka
package infrastructure.db.postgres.repositories.users

import java.util.UUID
import org.psyd.oselka.auth.users.User
import org.psyd.oselka.auth.users.UserId

final case class UserRow(id: UUID, name: String, hash: String) {
  def toUser = User(UserId(id), name, hash)
}
