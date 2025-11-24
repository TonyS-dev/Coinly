package com.coinly.user.domain.service

import com.coinly.common.exception.UserAlreadyExistsException
import com.coinly.security.TokenProvider
import com.coinly.user.domain.model.User
import com.coinly.user.domain.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authenticationManager: AuthenticationManager,
    private val tokenProvider: TokenProvider
) {
    fun register(username: String, email: String, password: String): User {
        if (userRepository.usernameExists(username)) {
            throw UserAlreadyExistsException("Username '$username' is already taken.")
        }
        if (userRepository.emailExists(email)) {
            throw UserAlreadyExistsException("Email '$email' is already registered.")
        }

        val newUser = User(
            id = UUID.randomUUID(),
            username = username,
            email = email,
            userRole = "USER"
        )

        val hashedPassword = passwordEncoder.encode(password)

        return userRepository.save(newUser, hashedPassword)
    }

    fun login(email: String, password: String): String {
        try {
            authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(email, password)
            )

            val user = userRepository.findByEmail(email)
                ?: throw IllegalStateException("User not found after successful authentication.")

            return tokenProvider.generateToken(user)
        } catch (e: AuthenticationException) {
            throw e
        }
    }
}