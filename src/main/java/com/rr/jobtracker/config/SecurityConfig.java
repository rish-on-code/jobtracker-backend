
package com.rr.jobtracker.config;

import com.rr.jobtracker.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // CORS configuration
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:5173",
                        "http://localhost:5174",
                        "https://jobtracker-frontend-bice.vercel.app")
        );

        configuration.setAllowedMethods(
                List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
        );

        configuration.setAllowedHeaders(
                List.of("Authorization", "Content-Type")
        );

        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                // Enable CORS
                .cors(Customizer.withDefaults())

                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/", "/users/register", "/users/login"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/users")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/jobs")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/jobs/**")
                        .hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/jobs/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PATCH, "/applications/*/status"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/applications")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET, "/applications/status/**"
                        ).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/dashboard/**")
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.GET, "/jobs", "/jobs/*"
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}
