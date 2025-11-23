package com.coinly.transaction.domain.service

import com.coinly.transaction.domain.model.Transaction
import com.coinly.transaction.domain.model.TransactionType
import com.coinly.transaction.domain.repository.TransactionRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Service
class TransactionService(private val transactionRepository: TransactionRepository) {

    fun createTransaction(
        userId: UUID,
        amount: BigDecimal,
        type: TransactionType,
        category: String,
        description: String?,
        transactionDate: Instant
    ): Transaction {
        val transaction = Transaction(
            id = UUID.randomUUID(),
            userId = userId,
            amount = amount,
            type = type,
            category = category,
            description = description,
            transactionDate = transactionDate
        )
        return transactionRepository.save(transaction)
    }

    fun getTransactionsForUser(userId: UUID): List<Transaction> {
        return transactionRepository.findAllByUserId(userId)
    }

    fun deleteTransaction(transactionId: UUID, userId: UUID) {
        val transaction = transactionRepository.findById(transactionId)
        // Business Rule: A user can only delete their own transactions
        if (transaction != null && transaction.userId == userId) {
            transactionRepository.deleteById(transactionId)
        } else {
            // Throw an exception (e.g., TransactionNotFoundException or NotAuthorizedException)
            throw IllegalStateException("Transaction not found or user not authorized.")
        }
    }
}