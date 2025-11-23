package com.coinly.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun coinlyOpenAPI(): OpenAPI {
        val devServer = Server().apply {
            url = "http://localhost:8080/api"
            description = "Development server"
        }

        val contact = Contact().apply {
            name = "Antonio Santiago"
            url = "https://github.com/TonyS-dev"
            email = "santiagor.acarlos@gmail.com"
        }

        val license = License().apply {
            name = "MIT License"
            url = "https://opensource.org/licenses/MIT"
        }

        val info = Info().apply {
            title = "Coinly API"
            version = "1.0.0"
            description = "API for managing personal finances, transactions, and budgets."
            this.contact = contact
            this.license = license
        }

        return OpenAPI()
            .info(info)
            .servers(listOf(devServer))
    }
}