package com.coinly.transaction.domain.repository

import com.coinly.transaction.domain.model.Transaction
import java.util.UUID

/**
 * Outbound Port for transaction persistence.
 */
interface TransactionRepository {
    fun save(transaction: Transaction): Transaction
    fun findById(id: UUID): Transaction?
    fun findAllByUserId(userId: UUID): List<Transaction>
    fun deleteById(id: UUID)
}