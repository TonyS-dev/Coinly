package com.coinly.transaction.infrastructure.controller

import com.coinly.common.exceptions.ResourceNotFoundException
import com.coinly.transaction.domain.service.TransactionService
import com.coinly.transaction.infrastructure.controller.dto.CreateTransactionRequest
import com.coinly.transaction.infrastructure.controller.dto.TransactionResponse
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
}