package com.coinly.common.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * A generic exception for when a user is not authorized to perform a certain action.
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
class NotAuthorizedException(message: String) : RuntimeException(message)