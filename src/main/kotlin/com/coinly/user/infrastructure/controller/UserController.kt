package com.coinly.user.infrastructure.controller

import com.coinly.common.exception.ResourceNotFoundException
import com.coinly.user.domain.repository.UserRepository
import com.coinly.user.infrastructure.controller.dto.UserResponse
import com.coinly.user.infrastructure.mapper.UserMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Tag(name = "3. Users", description = "Endpoints for user information")
@SecurityRequirement(name = "bearerAuth")
class UserController(
    private val userRepository: UserRepository,
    private val mapper: UserMapper
) {
    @Operation(summary = "Get current authenticated user's profile")
    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: UserDetails): UserResponse {
        val userDomain = userRepository.findByEmail(userDetails.username)
            ?: throw ResourceNotFoundException("Authenticated user not found")

        return mapper.toResponse(userDomain)
    }
}