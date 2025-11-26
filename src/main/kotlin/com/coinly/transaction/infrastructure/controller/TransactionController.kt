package com.coinly.transaction.infrastructure.controller

import com.coinly.common.exception.ResourceNotFoundException
import com.coinly.transaction.domain.service.TransactionService
import com.coinly.transaction.infrastructure.controller.dto.CreateTransactionRequest
import com.coinly.transaction.infrastructure.controller.dto.TransactionResponse
import com.coinly.transaction.infrastructure.controller.dto.UpdateTransactionRequest
import com.coinly.transaction.infrastructure.mapper.TransactionMapper
import com.coinly.user.domain.repository.UserRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import java.util.UUID

@RestController
@Tag(name = "2. Transactions", description = "Endpoints for managing transactions")
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService,
    private val userRepository: UserRepository,
    private val mapper: TransactionMapper
) {
    @Operation(summary = "Create a new transaction for the authenticated user")
    @ApiResponses(value = [
        ApiResponse(responseCode = "201", description = "Transaction created successfully"),
        ApiResponse(responseCode = "400", description = "Invalid transaction data provided")
    ])
    @PostMapping
    fun createTransaction(
        @Valid @RequestBody request: CreateTransactionRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): ResponseEntity<TransactionResponse> {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw ResourceNotFoundException("Authenticated user not found")

        val newTransaction = transactionService.createTransaction(
            userId = user.id,
            amount = request.amount,
            type = request.type,
            category = request.category,
            description = request.description,
            transactionDate = request.transactionDate
        )
        val responseDto = mapper.toResponse(newTransaction)

        return ResponseEntity(responseDto, HttpStatus.CREATED)
    }

    @Operation(summary = "Get all transactions for the authenticated user")
    @GetMapping
    fun getUserTransactions(@AuthenticationPrincipal userDetails: UserDetails): List<TransactionResponse> {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw ResourceNotFoundException("Authenticated user not found")

        val transactions = transactionService.getTransactionsForUser(user.id)
        return transactions.map { mapper.toResponse(it) }
    }

    @Operation(summary = "Update an existing transaction")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Transaction updated successfully"),
        ApiResponse(responseCode = "404", description = "Transaction not found"),
        ApiResponse(responseCode = "403", description = "User not authorized to update this transaction")
    ])
    @PutMapping("/{transactionId}")
    fun updateTransaction(
        @PathVariable transactionId: UUID,
        @Valid @RequestBody request: UpdateTransactionRequest,
        @AuthenticationPrincipal userDetails: UserDetails
    ): TransactionResponse {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw ResourceNotFoundException("Authenticated user not found")

        val updatedTransaction = transactionService.updateTransaction(
            transactionId = transactionId,
            userId = user.id,
            amount = request.amount,
            type = request.type,
            category = request.category,
            description = request.description,
            transactionDate = request.transactionDate
        )

        return mapper.toResponse(updatedTransaction)
    }

    @Operation(summary = "Delete a transaction")
    @ApiResponses(value = [
        ApiResponse(responseCode = "204", description = "Transaction deleted successfully"),
        ApiResponse(responseCode = "404", description = "Transaction not found"),
        ApiResponse(responseCode = "403", description = "User not authorized to delete this transaction")
    ])
    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTransaction(
        @PathVariable transactionId: UUID,
        @AuthenticationPrincipal userDetails: UserDetails
    ) {
        val user = userRepository.findByEmail(userDetails.username)
            ?: throw ResourceNotFoundException("Authenticated user not found")

        transactionService.deleteTransaction(transactionId, user.id)
    }
}