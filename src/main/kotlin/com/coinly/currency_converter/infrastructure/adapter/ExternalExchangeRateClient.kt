package com.coinly.currency_converter.infrastructure.adapter

import com.coinly.currency_converter.domain.port.out.ExchangeRateProviderPort
import com.coinly.currency_converter.infrastructure.controller.dto.ExternalRatesResponse
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Component
class ExternalExchangeRateClient(
    private val restTemplate: RestTemplate,
    @param:Value("\${external.exchange-rate-api.url}") private val apiUrl: String
) : ExchangeRateProviderPort {

    private val logger = LoggerFactory.getLogger(javaClass)

    @CircuitBreaker(name = "exchangeRateService", fallbackMethod = "getRatesFallback")
    @Cacheable("exchangeRates") // Cache name
    override fun getRates(baseCurrency: String): Map<String, BigDecimal> {
        logger.info("Fetching exchange rates for base currency: $baseCurrency from external API.")
        val url = "$apiUrl/$baseCurrency"
        val response = restTemplate.getForObject(url, ExternalRatesResponse::class.java)
        return response?.rates ?: throw IllegalStateException("Failed to fetch rates from external API.")
    }

    // This method is triggered if the external API fails or if it is slow
    private fun getRatesFallback(baseCurrency: String, ex: Exception): Map<String, BigDecimal> {
        logger.error("External exchange rate service is unavailable. Falling back. Error: ${ex.message}")
        // Return some default/fallback rates or an empty map
        return mapOf(
            "USD" to BigDecimal.ONE,
            "EUR" to BigDecimal("0.9"),
            "GBP" to BigDecimal("0.8")
        )
    }
}