package com.coinly.currency_converter.domain.port.out

import java.math.BigDecimal

/**
 * Outbound port.
 * Defines the contract that any exchange rate provider must implement.
 */
interface ExchangeRateProviderPort {
    /**
     * Retrieves all exchange rates for a given base currency.
     * @return a map from currency code to its rate.
     */
    fun getRates(baseCurrency: String): Map<String, BigDecimal>
}