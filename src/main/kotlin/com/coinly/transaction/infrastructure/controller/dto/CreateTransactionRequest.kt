package com.coinly.transaction.infrastructure.controller.dto

import com.coinly.transaction.domain.model.TransactionType
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.math.BigDecimal
import java.time.Instant

/**
 * DTO for creating a new transaction.
 * This is the data the client sends to the server.
 * Note the use of validation annotations.
 */
data class CreateTransactionRequest(
    @field:NotNull(message = "Amount cannot be null")
    @field:DecimalMin(value = "0.01", message = "Amount must be positive")
    var amount: BigDecimal,

    @field:NotNull(message = "Type cannot be null")
    var type: TransactionType,

    @field:NotBlank(message = "Category cannot be blank")
    @field:Size(max = 100, message = "Category cannot be longer than 100 characters")
    val category: String,

    @field:Size(max = 255, message = "Description cannot be longer than 255 characters")
    val description: String?,

    @field:NotNull(message = "Transaction date cannot be null")
    var transactionDate: Instant
)