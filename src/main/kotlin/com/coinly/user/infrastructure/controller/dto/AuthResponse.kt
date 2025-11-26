package com.coinly.user.infrastructure.controller.dto

// This DTO represents the response returned after a successful authentication,
// containing the JWT token.
data class AuthResponse(val token: String)