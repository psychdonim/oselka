package org.psyd.oselka
package infrastructure.db.postgres.config

import cats.effect.Sync

final case class PostgresConfig(
    host: String,
    port: Int,
    database: String,
    user: String,
    password: String
  )

object PostgresConfig {
  def loadF[F[_]: Sync]: F[PostgresConfig] = {
    val config = load
    config match {
      case Left(err) => Sync[F] raiseError { new RuntimeException(s"DB config error: $err") }
      case Right(value) => Sync[F] pure value
    }
  }

  private def load: Either[ConfigError, Config] = for {
    host <- fromEnv("PSYD_OSELKA_POSTGRES_HOST")
    portStr <- fromEnv("PSYD_OSELKA_POSTGRES_PORT")
    port <- portStr.toIntOption.toRight(InvalidPort(portStr))
    database <- fromEnv("PSYD_OSELKA_POSTGRES_DB")
    user <- fromEnv("PSYD_OSELKA_POSTGRES_USER")
    password <- fromEnv("PSYD_OSELKA_POSTGRES_PASSWORD")
  } yield Config(host, port.toInt, database, user, password)

  private def fromEnv(key: String): Either[ConfigError, String] =
    sys.env.get(key).toRight(MissingEnv(key))
}
