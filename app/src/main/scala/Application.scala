package org.psyd.oselka
package app

import cats.effect.{IO, IOApp}
import cats.effect.ExitCode
import org.psyd.oselka.app.http.apis.Auth
import org.psyd.oselka.auth.AuthService
import org.psyd.oselka.infrastructure.db.postgres.repositories.users.DoobieUsersRepository
import org.psyd.oselka.infrastructure.db.postgres.Database
import org.psyd.oselka.infrastructure.db.postgres.config.Config
import org.psyd.oselka.app.http.HttpServer
import org.psyd.oselka.app.http.ServerConfig
import cats.effect.Resource
import org.psyd.oselka.infrastructure.crypto.ArgonHasher
import org.psyd.oselka.infrastructure.jwt.Auth0JwtService
import org.http4s.server.Server

object Application extends IOApp.Simple {
  override def run: IO[Unit] = {
    IO.pure(())
  }

  private def resource(dbConfig: Config): Resource[IO, Server] = {
    for {
      xa <- Database.transactor[IO](dbConfig)
      
      users = DoobieUsersRepository[IO](xa)
      hasher = ArgonHasher[IO]
      jwtService = Auth0JwtService[IO]("secret")

      authService = AuthService[IO](users, hasher, jwtService)
      authApi = Auth[IO](authService)

      server <- HttpServer.run(authApi.endpoints, ServerConfig.load())
    } yield server
  }
}
