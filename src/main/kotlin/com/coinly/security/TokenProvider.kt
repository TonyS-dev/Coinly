package com.coinly.security

import com.coinly.user.domain.model.User

/**
 * Domain Port for token generation and validation.
 * Defines the CONTRACT that any token provider must adhere to.
 */
interface TokenProvider {
    fun generateToken(user: User): String
}