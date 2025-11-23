package com.coinly.transaction.infrastructure.persistence

import com.coinly.transaction.domain.model.TransactionType
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID
import org.hibernate.annotations.SoftDelete


@Entity
@SoftDelete
@Table(name = "transactions")
data class TransactionEntity(
    @Id
    val id: UUID,

    @Column(name = "user_id", nullable = false)
    val userId: UUID,

    @Column(nullable = false)
    val amount: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    val type: TransactionType,

    @Column(nullable = false)
    val category: String,

    val description: String?,

    @Column(name = "transaction_date", nullable = false)
    val transactionDate: Instant,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: Instant?,

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    val updatedAt: Instant?
)