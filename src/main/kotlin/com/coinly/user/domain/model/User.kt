package com.coinly.user.domain.model

import java.util.UUID

/**
 * PURE Domain Model for a User.
 * Represents a user within the core business logic.
 * It is immutable and has no knowledge of persistence.
 */
data class User(
    val id: UUID,
    val username: String,
    val email: String,
    val userRole: String
)