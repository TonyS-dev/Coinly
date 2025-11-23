package com.coinly.user.infrastructure.adapter

import com.coinly.user.domain.model.User
import com.coinly.user.domain.repository.UserRepository
import com.coinly.user.infrastructure.mapper.UserMapper
import com.coinly.user.infrastructure.persistence.SpringDataJpaUserRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository("userRepository")
class UserRepositoryAdapter(
    private val jpaRepository: SpringDataJpaUserRepository,
    private val mapper: UserMapper
) : UserRepository {

    override fun save(user: User, passwordHash: String): User {
        val entity = mapper.toEntity(user, passwordHash)
        val savedEntity = jpaRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun findByEmail(email: String): User? {
        return jpaRepository.findByEmail(email)?.let { mapper.toDomain(it) }
    }

    override fun findByUsername(username: String): User? {
        return jpaRepository.findByUsername(username)?.let { mapper.toDomain(it) }
    }

    override fun findById(id: UUID): User? {
        return jpaRepository.findById(id).orElse(null)?.let { mapper.toDomain(it) }
    }

    override fun emailExists(email: String): Boolean {
        return jpaRepository.existsByEmail(email)
    }

    override fun usernameExists(username: String): Boolean {
        return jpaRepository.existsByUsername(username)
    }
}