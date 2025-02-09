package com.ravindercodes.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravindercodes.constant.SecurityConstants;
import com.ravindercodes.exception.custom.UnAuthorizedEx;
import com.ravindercodes.exception.response.ErrorResponse;
import com.ravindercodes.security.serviceimpl.UserDetailsServiceImpl;
import com.ravindercodes.util.JwtUtility;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        if (SecurityConstants.AUTH_WHITELIST.stream()
                .map(url -> url.replace("/**", ""))
                .anyMatch(requestURI::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = parseJwt(request);
            if (jwt == null) {
                throw new UnAuthorizedEx("Token is missing", HttpStatus.UNAUTHORIZED);
            }

            if (jwtUtility.isValidToken(jwt)) {
                String username = jwtUtility.getUserNameFromJwtToken(jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new UnAuthorizedEx("Token is invalid", HttpStatus.UNAUTHORIZED);
            }
        } catch (UnAuthorizedEx e) {
            log.error("Unauthorized access: {}", e.getMessage());
            sendErrorResponse(response, e.getMessage(), e.getClass().getName(), request.getRequestURI());
            return;
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            sendErrorResponse(response, "Invalid token", e.getClass().getName(), request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }

    private void sendErrorResponse(HttpServletResponse response, String message, String exception, String path) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(System.currentTimeMillis() / 1000);
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setError("Unauthorized");
        errorResponse.setException(exception);
        errorResponse.setMessage(message);
        errorResponse.setPath(path);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
