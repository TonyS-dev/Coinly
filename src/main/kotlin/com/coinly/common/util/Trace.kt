package com.coinly.common.util

import org.slf4j.MDC
import java.util.Optional
import java.util.UUID

/**
 * Utility for tracing requests. In a real microservice architecture,
 * this traceId would be propagated across service calls.
 */
object Trace {
    fun currentId(): String {
        return Optional.ofNullable(MDC.get("traceId"))
            .orElse(UUID.randomUUID().toString())
    }
}