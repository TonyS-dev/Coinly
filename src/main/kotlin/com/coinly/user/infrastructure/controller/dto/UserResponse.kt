package com.coinly.user.infrastructure.controller.dto

import java.util.UUID

// This DTO represents the user information returned in responses,
// excluding sensitive data like passwords.
data class UserResponse(
    val id: UUID,
    val username: String,
    val email: String,
    val userRole: String
)