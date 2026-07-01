package org.psyd.oselka
package infrastructure.db.postgres.config

sealed trait ConfigError

case class MissingEnv(key: String) extends ConfigError
case class InvalidPort(actual: String) extends ConfigError

