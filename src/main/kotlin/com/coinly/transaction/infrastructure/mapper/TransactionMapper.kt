package com.coinly.transaction.infrastructure.mapper

import com.coinly.transaction.domain.model.Transaction
import com.coinly.transaction.infrastructure.controller.dto.TransactionResponse
import com.coinly.transaction.infrastructure.persistence.TransactionEntity
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface TransactionMapper {
    fun toDomain(entity: TransactionEntity): Transaction

    fun toResponse(domain: Transaction): TransactionResponse

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    fun toEntity(domain: Transaction): TransactionEntity
}