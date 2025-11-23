package com.coinly.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class JwtAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        // When an unauthenticated user tries to access a secured endpoint,
        // this method is invoked.
        response.status = HttpServletResponse.SC_UNAUTHORIZED
        response.contentType = "application/problem+json"

        val pd = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, "Authentication required. Please log in to access this resource.")
        pd.title = "Unauthorized"
        pd.setProperty("timestamp", Instant.now())
        pd.setProperty("path", request.requestURI)

        // The response body will contain a JSON representation of the ProblemDetail
        response.writer.write(objectMapper.writeValueAsString(pd))
    }
}