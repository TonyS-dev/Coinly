package com.coinly.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * A generic exception for when a requested resource (like a Transaction or User) cannot be found.
 * Maps to an HTTP 404 Not Found status.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String) : RuntimeException(message)