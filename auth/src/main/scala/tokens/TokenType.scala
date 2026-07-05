package org.psyd.oselka
package auth.tokens

enum TokenType {
  case Access extends TokenType
  case Refresh extends TokenType
}
