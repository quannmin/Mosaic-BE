package com.mosaic.configuration;

import com.mosaic.filter.SpaWebFilter;
import com.mosaic.service.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    private static final String[] PUBLIC_URLS = {
            "/swagger-ui/**",
            "/api-docs/**",
            "/v1/api-docs/**",
            "/api/v1/auth/**",
            "/api/v1/public/**",
            "/",
            "/index.html",
            "/static/**",
            "/assets/**",
            "/*.js",
            "/*.css",
            "/*.ico"
    };

    private static final String[] ADMIN_URLS = {
            "/api/v1/admin/**",
            "/api/v1/management/**",
            "/api/v1/reports/**",
            "/api/v1/users/**",
            "/api/v1/addresses/**",
    };

    private static final String[] MANAGER_URLS = {
            "/api/v1/products/**",
            "/api/v1/product-variants/**",
            "/api/v1/orders/**",
            "api/v1/quantity-discounts/**",
            "/api/v1/images/**",
    };

    private static final String[] USER_URLS = {
            "/api/v1/products/**",
            "/api/v1/product-variants/**",
            "/api/v1/orders/**",
            "/api/v1/users/**",
    };
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .addFilterAfter(new SpaWebFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(
                    request -> request
                            .requestMatchers(PUBLIC_URLS).permitAll()
                            .requestMatchers(USER_URLS).permitAll()
                            .requestMatchers(ADMIN_URLS).hasRole("ADMIN")
                            .requestMatchers(MANAGER_URLS).hasAnyRole("ADMIN", "MANAGER")
                            .anyRequest().authenticated()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .formLogin(AbstractHttpConfigurer::disable)
                .oauth2ResourceServer(oauth2 ->
                        oauth2
                                .jwt(Customizer.withDefaults())
                                .authenticationEntryPoint(authenticationEntryPoint)
                );        return http.build();
    }
}
