package org.propertybiddingsystem.usermanagementsystem.Config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.propertybiddingsystem.usermanagementsystem.Utils.JwtUtils;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Extract the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // 2. Guard: if no header or wrong format, skip and continue filter chain
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Extract the JWT token (everything after "Bearer ")
        final String jwt = authHeader.substring(7);
        final String userEmail;

        try {
            // 4. Extract username (email) from token
            userEmail = jwtUtils.extractUserName(jwt);

            // 5. If valid username and user is not already authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // 6. Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                // 7. Validate token against the loaded user
                if (jwtUtils.isTokenValid(jwt, userDetails)) {

                    // 8. Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    // 9. Add request details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // 10. Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token is invalid/expired - just continue without authentication
            // Spring Security will handle the 401 response for protected endpoints
        }

        // 11. Continue the filter chain
        filterChain.doFilter(request, response);
    }
}