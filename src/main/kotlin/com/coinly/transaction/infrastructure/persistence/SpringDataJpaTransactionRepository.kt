package com.coinly.transaction.infrastructure.persistence

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SpringDataJpaTransactionRepository : JpaRepository<TransactionEntity, UUID> {
    fun findAllByUserIdOrderByTransactionDateDesc(userId: UUID): List<TransactionEntity>
}