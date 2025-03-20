package com.effix.api.config;

import com.effix.api.components.RestAccessDeniedHandler;
import com.effix.api.components.RestAuthenticationEntryPoint;
import com.effix.api.security.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.time.LocalDateTime;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtTokenFilter jwtRequestFilter;

    @Bean
    public RestAuthenticationEntryPoint unauthorizedHandler(){
        return new RestAuthenticationEntryPoint();
    }

    @Bean
    public RestAccessDeniedHandler accessDeniedHandler(){
        return new RestAccessDeniedHandler();
    }

    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(10000);
        filter.setIncludeHeaders(false);
        filter.setAfterMessagePrefix("Timestamp: " + LocalDateTime.now() + ", REQUEST DATA: ");
        return filter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable() // Disable CSRF
                .authorizeRequests()
                .antMatchers("/api/v1/users/logout").authenticated() // Require authentication for /api/v1/users/logout
                .antMatchers("/api/v1/users/**", "/api/v1/profiles/payment/webhook", "/api/v1/health").permitAll()
                .anyRequest().authenticated() // Any other request needs authentication
                .and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler()).authenticationEntryPoint(unauthorizedHandler())
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Use stateless session, as we are using JWTs

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://client-app-ui-two.vercel.app"));
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowCredentials(true);
        configuration.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}

