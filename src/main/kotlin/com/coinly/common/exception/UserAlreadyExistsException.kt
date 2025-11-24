package com.coinly.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Thrown when attempting to register a user with an email or username that already exists.
 * Maps to an HTTP 409 Conflict status.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class UserAlreadyExistsException(message: String) : RuntimeException(message)
