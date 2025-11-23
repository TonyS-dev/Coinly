package com.coinly.transaction.infrastructure.adapter

import com.coinly.transaction.domain.model.Transaction
import com.coinly.transaction.domain.repository.TransactionRepository
import com.coinly.transaction.infrastructure.mapper.TransactionMapper
import com.coinly.transaction.infrastructure.persistence.SpringDataJpaTransactionRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
class TransactionRepositoryAdapter(
    private val jpaRepository: SpringDataJpaTransactionRepository,
    private val mapper: TransactionMapper
) : TransactionRepository {

    override fun save(transaction: Transaction): Transaction {
        val entity = mapper.toEntity(transaction)
        val savedEntity = jpaRepository.save(entity)
        return mapper.toDomain(savedEntity)
    }

    override fun findById(id: UUID): Transaction? {
        return jpaRepository.findById(id).orElse(null)?.let { mapper.toDomain(it) }
    }

    override fun findAllByUserId(userId: UUID): List<Transaction> {
        return jpaRepository.findAllByUserIdOrderByTransactionDateDesc(userId)
            .map { mapper.toDomain(it) }
    }

    override fun deleteById(id: UUID) {
        jpaRepository.deleteById(id)
    }
}