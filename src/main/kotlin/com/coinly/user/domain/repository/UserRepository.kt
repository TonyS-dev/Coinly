package com.coinly.user.domain.repository

import com.coinly.user.domain.model.User
import java.util.UUID

/**
 * Outbound Port (Repository Port).
 * Defines the contract for user persistence operations.
 * The Domain Layer depends on this interface.
 */
interface UserRepository {
    fun save(user: User, passwordHash: String): User
    fun findByEmail(email: String): User?
    fun findByUsername(username: String): User?
    fun findById(id: UUID): User?
    fun emailExists(email: String): Boolean
    fun usernameExists(username: String): Boolean
}