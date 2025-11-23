package com.coinly.user.infrastructure.security

import com.coinly.user.infrastructure.persistence.SpringDataJpaUserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.security.core.userdetails.User

@Service
class UserDetailsServiceImpl(
    private val jpaUserRepository: SpringDataJpaUserRepository
) : UserDetailsService {
    override fun loadUserByUsername(email: String): UserDetails {
        val userEntity = jpaUserRepository.findByEmail(email)
            ?: throw UsernameNotFoundException("User not found")

        return User
            .withUsername(userEntity.email)
            .password(userEntity.passwordHash)
            .authorities(userEntity.userRole)
            .build()
    }
}