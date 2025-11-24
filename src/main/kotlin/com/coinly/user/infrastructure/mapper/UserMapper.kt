package com.coinly.user.infrastructure.mapper

import com.coinly.user.domain.model.User
import com.coinly.user.infrastructure.controller.dto.UserResponse
import com.coinly.user.infrastructure.persistence.UserEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.Mappings

@Mapper(componentModel = "spring")
interface UserMapper {
    fun toDomain(entity: UserEntity): User

    fun toResponse(domain: User): UserResponse

    @Mappings(
        Mapping(target = "createdAt", ignore = true),
        Mapping(target = "updatedAt", ignore = true),
        Mapping(target = "passwordHash", source = "passwordHash")
    )
    fun toEntity(domain: User, passwordHash: String): UserEntity
}