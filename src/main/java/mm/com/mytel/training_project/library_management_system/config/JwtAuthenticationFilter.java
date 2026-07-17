package mm.com.mytel.training_project.library_management_system.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import mm.com.mytel.training_project.library_management_system.common.util.JwtUtils;
import mm.com.mytel.training_project.library_management_system.common.util.TokenBlockUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtil;
    private final TokenBlockUtil tokenBlockUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = jwtUtil.resolveBearerToken(request.getHeader("Authorization"));

        if (token != null) {
            try {
                if (tokenBlockUtil.isBlocked(token)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("Unauthorized: Token has been removed");
                    return;
                }

                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(username, null, null);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Unauthorized: Invalid token");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
