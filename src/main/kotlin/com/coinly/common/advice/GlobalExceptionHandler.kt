package com.coinly.common.advice

import com.coinly.common.exception.ResourceNotFoundException
import com.coinly.common.exception.UserAlreadyExistsException
import com.coinly.common.util.Trace
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = LoggerFactory.getLogger(javaClass)

    // Error 404: when a requested resource is not found
    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleResourceNotFound(ex: ResourceNotFoundException, request: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.localizedMessage)
        pd.title = "Resource Not Found"
        pd.setProperty("traceId", Trace.currentId())
        logger.warn("TraceID[${Trace.currentId()}]: ${ex.message}")
        return pd
    }

    // Error 409: Conflict, e.g., when trying to create a resource that already exists
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserConflict(ex: UserAlreadyExistsException, request: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.localizedMessage)
        pd.title = "Data Conflict"
        pd.setProperty("traceId", Trace.currentId())
        logger.warn("TraceID[${Trace.currentId()}]: ${ex.message}")
        return pd
    }

    // Error 400: Dto validation errors
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationErrors(ex: MethodArgumentNotValidException): ProblemDetail {
        val errors = ex.bindingResult.fieldErrors
            .associate { it.field to (it.defaultMessage ?: "Invalid value") }

        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Validation Failed"
        pd.detail = "One or more fields are invalid."
        pd.setProperty("errors", errors)
        pd.setProperty("traceId", Trace.currentId())
        return pd
    }

    // Error 400: When the JSON in the request body is malformed
    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedJson(ex: HttpMessageNotReadableException): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Malformed JSON Request"
        pd.detail = "The request body could not be parsed."
        pd.setProperty("traceId", Trace.currentId())
        return pd
    }

    // Error 401: Authentication failures
    @ExceptionHandler(AuthenticationException::class)
    fun handleAuthenticationException(ex: AuthenticationException): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED)
        pd.title = "Authentication Failed"
        pd.detail = "Invalid credentials provided."
        pd.setProperty("traceId", Trace.currentId())
        return pd
    }

    // Error 500: Generic server error handler
    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception, request: HttpServletRequest): ProblemDetail {
        val traceId = Trace.currentId()
        val pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        pd.title = "Internal Server Error"
        pd.detail = "An unexpected error occurred. Please report this with trace ID: $traceId"
        pd.setProperty("traceId", traceId)
        logger.error("TraceID[$traceId]: Unhandled exception at ${request.requestURI}", ex)
        return pd
    }
}