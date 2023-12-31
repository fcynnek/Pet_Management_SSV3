package com.fcynnek.PetManagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fcynnek.PetManagement.domain.Role;
import com.fcynnek.PetManagement.service.UserService;
@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
//    private final LoginSuccessHandler loginSuccessHandler;
    
    public SecurityConfig (JwtAuthenticationFilter jwtAuthenticationFilter, UserService userService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userService = userService;
//        this.loginSuccessHandler = loginSuccessHandler;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
//        .authorizeHttpRequests(request -> request.requestMatchers("**").permitAll().anyRequest().authenticated())
                .authorizeHttpRequests((request) -> {request
//                              .requestMatchers("/api/v1/auth/**").permitAll()
//                				.requestMatchers(HttpMethod.GET, "/**").permitAll()
                				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                                .requestMatchers("/").permitAll()
                                .requestMatchers("/about").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/dashboard").authenticated()
//                              .requestMatchers("/admin/**").hasRole(Role.ADMIN.name())
//                              .requestMatchers("/dashboard").authenticated()
                                .anyRequest().permitAll();
                })
//              .headers(header -> header.frameOptions(frameOption -> frameOption.disable()))
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> {
                	login.loginPage("/register");
                	login.failureUrl("/error");
//                	login.successHandler(loginSuccessHandler);
                	login.permitAll();
                });
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }
}