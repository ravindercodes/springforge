package com.ravindercodes.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ravindercodes.constant.SecurityConstants;
import com.ravindercodes.filter.AuthTokenFilter;
import com.ravindercodes.repository.UserRepository;
import com.ravindercodes.repository.UserSessionRepository;
import com.ravindercodes.security.serviceimpl.UserDetailsServiceImpl;
import com.ravindercodes.util.JwtUtility;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private static final String[] AUTH_WHITELIST = SecurityConstants.AUTH_WHITELIST.toArray(new String[0]);
    private final JwtUtility jwtUtility;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final HttpServletRequest request;
    private final UserSessionRepository userSessionRepository;

    public SecurityConfig(JwtUtility jwtUtility, UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper, UserRepository userRepository, ModelMapper modelMapper, HttpServletRequest request, UserSessionRepository userSessionRepository) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.request = request;
        this.userSessionRepository = userSessionRepository;
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter(jwtUtility, userDetailsService, objectMapper);
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public OAuthSuccessHandler oAuth2SuccessHandler() {
        return new OAuthSuccessHandler(userRepository, modelMapper, jwtUtility, request, userSessionRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(AUTH_WHITELIST).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/h2-ui/**")).permitAll()
                                .anyRequest().authenticated()
                ).oauth2Login(auth -> auth.successHandler(oAuth2SuccessHandler()));

        http.headers(headers -> headers.frameOptions(frameOption -> frameOption.sameOrigin()));

        http.authenticationProvider(authenticationProvider());

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
