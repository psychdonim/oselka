package org.psyd.oselka
package infrastructure.db.postgres

import cats.effect.Async
import org.psyd.oselka.infrastructure.db.postgres.config.Config
import cats.effect.Resource
import org.typelevel.doobie.hikari.HikariTransactor
import org.typelevel.doobie.util.ExecutionContexts
import org.typelevel.doobie.util.transactor.Transactor
import org.typelevel.doobie.free.driver

object Database {
  def transactor[F[_]: Async](cfg: Config): Resource[F, Transactor[F]] = {
    Resource.pure{
      Transactor.fromDriverManager[F](
          driver = "org.postgres.Driver",
          url = s"jdbc:postgresql://${cfg.host}:${cfg.port}/${cfg.database}",
          user = cfg.user,
          password = cfg.password,
          logHandler = None
        )
    }
  }
}
