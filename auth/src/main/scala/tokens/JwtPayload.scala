package org.psyd.oselka
package auth.tokens

import org.psyd.oselka.auth.users.UserId

final case class JwtPayload(userId: UserId, tokenType: TokenType)
