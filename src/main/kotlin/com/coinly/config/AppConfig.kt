package com.coinly.config

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class AppConfig {

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
        // Create a RestTemplate with default settings
        // Spring will manage it, and it can be injected it into adapters.
        // It can be customized here if needed (e.g., adding interceptors, message converters, etc.)
        return builder.build()
    }
}