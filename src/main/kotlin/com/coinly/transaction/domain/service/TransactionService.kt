package com.coinly.transaction.domain.service

import com.coinly.common.exception.NotAuthorizedException
import com.coinly.common.exception.ResourceNotFoundException
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

    fun updateTransaction(
        transactionId: UUID,
        userId: UUID,
        amount: BigDecimal?,
        type: TransactionType?,
        category: String?,
        description: String?,
        transactionDate: Instant?
    ): Transaction {
        val existingTransaction = transactionRepository.findById(transactionId)
            ?: throw ResourceNotFoundException("Transaction with ID $transactionId not found.")

        // Business Rule: A user can only update their own transactions
        if (existingTransaction.userId != userId) {
            throw NotAuthorizedException("User is not authorized to update this transaction.") // Crea esta excepción en common
        }

        val updatedTransaction = existingTransaction.copy(
            amount = amount ?: existingTransaction.amount,
            type = type ?: existingTransaction.type,
            category = category ?: existingTransaction.category,
            description = description ?: existingTransaction.description,
            transactionDate = transactionDate ?: existingTransaction.transactionDate
        )

        return transactionRepository.save(updatedTransaction)
    }

    fun deleteTransaction(transactionId: UUID, userId: UUID) {
        val transaction = transactionRepository.findById(transactionId)
            ?: throw ResourceNotFoundException("Transaction with ID $transactionId not found.")

        // Business rule: A user can only delete their own transactions
        if (transaction.userId != userId) {
            throw NotAuthorizedException("User is not authorized to delete this transaction.")
        }

        transactionRepository.deleteById(transactionId)
    }
}