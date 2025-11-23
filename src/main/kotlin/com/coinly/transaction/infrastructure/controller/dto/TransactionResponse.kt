package com.coinly.transaction.infrastructure.controller.dto

import com.coinly.transaction.domain.model.TransactionType
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

/**
 * DTO for the response sent back to the client.
 * This defines what the client will see.
 */
data class TransactionResponse(
    val id: UUID,
    val amount: BigDecimal,
    val type: TransactionType,
    val category: String,
    val description: String?,
    val transactionDate: Instant
)