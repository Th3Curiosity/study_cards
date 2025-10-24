package com.th3curiosity.studycards.config;

import com.th3curiosity.studycards.security.CustomAuthenticationEntryPoint;
import com.th3curiosity.studycards.security.JwtAuthFilter;
import com.th3curiosity.studycards.security.JwtUtils;
import com.th3curiosity.studycards.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    private final String[] whitelist = {
            "/v3/api-docs/**",
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/api/auth/**"
    };

    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          JwtUtils jwtUtils,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthFilter jwtAuthFilter = new JwtAuthFilter(jwtUtils, userDetailsService);

        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sm -> sm.
                        sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(whitelist).permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex.
                        authenticationEntryPoint(customAuthenticationEntryPoint));

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
