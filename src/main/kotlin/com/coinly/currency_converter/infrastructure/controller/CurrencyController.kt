package com.coinly.currency_converter.infrastructure.controller

import com.coinly.currency_converter.domain.model.ConvertedAmount
import com.coinly.currency_converter.domain.service.CurrencyConverterService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RestController
@RequestMapping("/api/currency")
@Tag(name = "4. Currency Conversion", description = "Endpoints for currency conversion")
class CurrencyController(private val currencyConverterService: CurrencyConverterService) {

    @Operation(summary = "Convert an amount from one currency to another")
    @GetMapping("/convert")
    fun convertCurrency(
        @Parameter(description = "Amount to convert", example = "100.00") @RequestParam amount: BigDecimal,
        @Parameter(description = "Base currency code (3 letters)", example = "USD") @RequestParam from: String,
        @Parameter(description = "Target currency code (3 letters)", example = "EUR") @RequestParam to: String
    ): ConvertedAmount {
        return currencyConverterService.convert(amount, from, to)
    }
}