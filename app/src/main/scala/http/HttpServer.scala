package org.psyd.oselka
package app.http

import sttp.tapir.server.ServerEndpoint
import sttp.tapir.server.http4s.Http4sServerInterpreter
import org.http4s.ember.server.EmberServerBuilder
import cats.effect.IO
import cats.effect.Async
import fs2.io.net.Network
import com.comcast.ip4s.Host
import com.comcast.ip4s.Port
import com.comcast.ip4s._
import cats.effect.Resource
import org.http4s.server.Server

object HttpServer {
  def run[F[_]: Async: Network](endpoints: List[ServerEndpoint[Any, F]], config: ServerConfig): Resource[F, Server] = {
     val routes = Http4sServerInterpreter[F]().toRoutes(endpoints)

     EmberServerBuilder
      .default[F]
      .withHost(host"localhost")
      .withPort(port"0")
      .withHttpApp(routes.orNotFound)
      .build
  }
}
