package com.coinly.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: UserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            // 1. Extract the token from the request header
            val jwt = getJwtFromRequest(request)

            // 2. Validate the token
            if (jwt != null && jwtTokenProvider.validateToken(jwt)) {
                // 3. Get user identity from the token
                val userEmail = jwtTokenProvider.getEmailFromToken(jwt)

                // 4. Load user details from the database
                val userDetails = userDetailsService.loadUserByUsername(userEmail)

                // 5. Create an authentication object
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                // 6. Set the user in Spring Security's context
                SecurityContextHolder.getContext().authentication = authentication
            }
        } catch (ex: Exception) {
            // In case of error, the authentication context is not set
            logger.error("Could not set user authentication in security context", ex)
        }

        // 7. Continue the filter chain
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7)
        }
        return null
    }
}