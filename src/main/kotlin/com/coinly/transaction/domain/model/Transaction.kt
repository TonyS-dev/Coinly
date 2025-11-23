package com.coinly.transaction.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * PURE Domain Model for a Transaction.
 */
data class Transaction(
    val id: UUID,
    val userId: UUID,
    val amount: BigDecimal,
    val type: TransactionType,
    val category: String,
    val description: String?,
    val transactionDate: Instant
)