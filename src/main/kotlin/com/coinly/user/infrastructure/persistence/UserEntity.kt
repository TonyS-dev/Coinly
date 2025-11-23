package com.coinly.user.infrastructure.persistence

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.SoftDelete

@Entity
@SoftDelete
@Table(name = "users")
data class UserEntity(
    @Id
    val id: UUID,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "user_role", nullable = false)
    val userRole: String,

    @Column(name = "is_active", nullable = false)
    val isActive: Boolean,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: Instant?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant?
)