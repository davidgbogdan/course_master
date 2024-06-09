package com.project.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;
    private final HandlerExceptionResolver handlerExceptionResolver;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            if (checkAuthorizationEndpoints(request)) {
                response.setStatus(401);
            } else {
                filterChain.doFilter(request, response);
            }

            return;
        }

        try {
            String jwt = authHeader.substring(7);
            String username = jwtProvider.extractUsername(jwt);

            if (username != null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (Exception exception) {
            handlerExceptionResolver.resolveException(request, response, null, exception);
            response.setStatus(401);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean checkAuthorizationEndpoints(HttpServletRequest request){
        boolean checkTeacherEndpoints = !request.getServletPath().contains("teachers");
        boolean checkCourseEndpoints = !request.getServletPath().contains("courses");
        boolean checkStudentEndpoints = !request.getServletPath().contains("students");

        return checkTeacherEndpoints
                && checkCourseEndpoints
                && checkStudentEndpoints;
    }
}
