package com.coinly.currency_converter.infrastructure.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class ExternalRatesResponse(
    val result: String,
    @get:JsonProperty("base_code") // It maps the JSON field "base_code" to the property "baseCode"
    val baseCode: String,
    val rates: Map<String, BigDecimal>
)