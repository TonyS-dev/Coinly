package com.coinly.user.infrastructure.mapper

import com.coinly.user.domain.model.User
import com.coinly.user.infrastructure.persistence.UserEntity
import org.springframework.stereotype.Component
import com.coinly.user.infrastructure.controller.dto.UserResponse

@Component
class UserMapper {
    fun toDomain(entity: UserEntity): User = User(
        id = entity.id,
        username = entity.username,
        email = entity.email,
        userRole = entity.userRole
    )

    fun toEntity(domain: User, passwordHash: String): UserEntity = UserEntity(
        id = domain.id,
        username = domain.username,
        email = domain.email,
        passwordHash = passwordHash,
        userRole = domain.userRole,
        isActive = true, // By default, a new user is active
        createdAt = null, // Let Hibernate manage the timestamps
        updatedAt = null
    )

    fun toResponse(domain: User): UserResponse = UserResponse(
        id = domain.id,
        username = domain.username,
        email = domain.email,
        userRole = domain.userRole
    )
}