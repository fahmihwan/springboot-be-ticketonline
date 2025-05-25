package ticket_online.ticket_online.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ticket_online.ticket_online.service.UserService;

import java.io.IOException;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                DecodedJWT decodedJWT = jwtUtil.decodedJWT(token);
                String email = decodedJWT.getSubject();
                String role = decodedJWT.getClaim("role").asString();

                UserDetails userDetails = new org.springframework.security.core.userdetails.User(email, "", List.of(new SimpleGrantedAuthority(role.toUpperCase())));


                GrantedAuthority authority = new SimpleGrantedAuthority(role.toUpperCase());
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, List.of(authority));


                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (TokenExpiredException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (JWTVerificationException e) {
                throw new RuntimeException(e.getMessage(), e);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }

        filterChain.doFilter(request, response);
    }



}
