package com.coinly.currency_converter.domain.service

import com.coinly.common.exception.ResourceNotFoundException
import com.coinly.currency_converter.domain.model.ConvertedAmount
import com.coinly.currency_converter.domain.port.out.ExchangeRateProviderPort
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.math.RoundingMode

@Service
class CurrencyConverterService(
    private val exchangeRateProvider: ExchangeRateProviderPort
) {
    fun convert(amount: BigDecimal, fromCurrency: String, toCurrency: String): ConvertedAmount {
        // Get all the rates for the base currency
        val rates = exchangeRateProvider.getRates(fromCurrency.uppercase())

        // Look for the target currency rate
        val rate = rates[toCurrency.uppercase()]
            ?: throw ResourceNotFoundException("Target currency '$toCurrency' not found for base '$fromCurrency'.")

        // Calculate the converted amount
        val convertedAmount = amount.multiply(rate).setScale(2, RoundingMode.HALF_UP)

        return ConvertedAmount(
            originalAmount = amount,
            fromCurrency = fromCurrency.uppercase(),
            toCurrency = toCurrency.uppercase(),
            convertedAmount = convertedAmount,
            rate = rate
        )
    }
}