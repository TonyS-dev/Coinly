package com.coinly.user.infrastructure.controller

import com.coinly.user.domain.service.AuthService
import com.coinly.user.infrastructure.controller.dto.AuthResponse
import com.coinly.user.infrastructure.controller.dto.LoginRequest
import com.coinly.user.infrastructure.controller.dto.RegisterRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import io.swagger.v3.oas.annotations.media.Schema

@RestController
@RequestMapping("/api/auth")
@Tag(name = "1. Authentication", description = "Endpoints for user registration and login")
class AuthController(private val authService: AuthService) {

    @Operation(summary = "Register a new user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "User registered successfully", content = [Content()]),
        ApiResponse(responseCode = "400", description = "Invalid input data", content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))]),
        ApiResponse(responseCode = "409", description = "Username or email is already taken", content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))])
    ])
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest): AuthResponse {
        authService.register(
            username = request.username,
            email = request.email,
            password = request.password
        )
        val token = authService.login(request.email, request.password)
        return AuthResponse(token)
    }

    @Operation(summary = "Log in a user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Login successful", content = [Content(schema = Schema(implementation = AuthResponse::class))]),
        ApiResponse(responseCode = "401", description = "Invalid credentials", content = [Content(mediaType = "application/problem+json", schema = Schema(implementation = ProblemDetail::class))])
    ])
    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest): AuthResponse {
        val token = authService.login(request.email, request.password)
        return AuthResponse(token)
    }
}