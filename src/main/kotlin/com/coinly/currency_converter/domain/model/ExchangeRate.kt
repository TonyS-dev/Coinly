package com.coinly.currency_converter.domain.model

import java.math.BigDecimal

/**
 * Domain Model for Exchange Rate between two currencies.
 */
data class ExchangeRate(
    val baseCurrency: String,
    val targetCurrency: String,
    val rate: BigDecimal
)

/**
 * Domain Model for Converted Amount after applying exchange rate.
 */
data class ConvertedAmount(
    val originalAmount: BigDecimal,
    val fromCurrency: String,
    val toCurrency: String,
    val convertedAmount: BigDecimal,
    val rate: BigDecimal
)