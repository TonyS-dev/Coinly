package com.coinly.transaction.infrastructure.controller.dto

import com.coinly.transaction.domain.model.TransactionType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant


data class UpdateTransactionRequest(
    @field:DecimalMin(value = "0.01", message = "Amount must be positive")
    val amount: BigDecimal?,

    val type: TransactionType?,

    @field:Size(max = 100)
    val category: String?,

    @field:Size(max = 255)
    val description: String?,

    val transactionDate: Instant?
)