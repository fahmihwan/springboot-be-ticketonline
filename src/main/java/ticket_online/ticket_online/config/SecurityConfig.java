package ticket_online.ticket_online.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import ticket_online.ticket_online.security.AuthTokenFilter;
import ticket_online.ticket_online.security.JwtAuthenticationEntryPoint;
import ticket_online.ticket_online.service.UserService;

import java.util.List;

@Configuration
@EnableMethodSecurity(prePostEnabled = true)// Aktifkan anotasi @PreAuthorize
public class SecurityConfig {

    @Autowired
    private AuthTokenFilter jwtFilter;

    @Autowired
    private UserService userService;


    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder())
                .and()
                .build();
    }

    @Bean //disini untuk set Auth GUest
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception { //CORS INI YANG TERPAKAI
        return http
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Menambahkan entry point untuk handle autentikasi yang gagal / kadaluarsa
                .and()
                .cors()
                .and()
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers( //untuk set no auth
                                "/uploaded-images/**",
                                "/api/auth/register-user",
                                "/api/auth/register-admin",
                                "/api/auth/login",
                                "/api/event/*",
                                "/api/event/*/events",
                                "/api/event/*/with-category-tickets",
                                "/api/transaction/callback",
                                "/api/transaction/checkout"
                                ).permitAll()
//                        .requestMatchers("/api/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }



    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        source.registerCorsConfiguration("/uploaded-images/**", config);
        return source;
    }

//    @Bean
//    public AuthTokenFilter authTokenFilter() {
//        return new AuthTokenFilter(); // atau pakai @Component kalau constructor-nya pakai @Autowired/@RequiredArgsConstructor
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
