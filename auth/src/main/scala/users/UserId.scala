package org.psyd.oselka
package auth.users

import java.util.UUID
import com.github.f4b6a3.uuid.UuidCreator

opaque type UserId = UUID

object UserId {
  def apply(id: UUID): UserId = id

  def random: UserId =
    UuidCreator.getTimeOrderedEpoch()

  extension (id: UserId) {
    def uuid: UUID = id
  }
}
