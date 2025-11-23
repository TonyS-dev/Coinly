package com.coinly.currency_converter.domain.model

data class ExchangeRate(val base: String = "USD", val rates: Map<String, Double> = emptyMap())

