package com.coinly.transaction.infrastructure.mapper

import com.coinly.transaction.domain.model.Transaction
import com.coinly.transaction.infrastructure.controller.dto.TransactionResponse
import com.coinly.transaction.infrastructure.persistence.TransactionEntity
import org.springframework.stereotype.Component

@Component
class TransactionMapper {
    fun toDomain(entity: TransactionEntity): Transaction = Transaction(
        id = entity.id,
        userId = entity.userId,
        amount = entity.amount,
        type = entity.type,
        category = entity.category,
        description = entity.description,
        transactionDate = entity.transactionDate
    )

    fun toEntity(domain: Transaction): TransactionEntity = TransactionEntity(
        id = domain.id,
        userId = domain.userId,
        amount = domain.amount,
        type = domain.type,
        category = domain.category,
        description = domain.description,
        transactionDate = domain.transactionDate,
        createdAt = null,
        updatedAt = null
    )

    fun toResponse(domain: Transaction): TransactionResponse = TransactionResponse(
        id = domain.id,
        amount = domain.amount,
        type = domain.type,
        category = domain.category,
        description = domain.description,
        transactionDate = domain.transactionDate
    )
}