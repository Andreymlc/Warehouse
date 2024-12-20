package com.example.Warehouse.config;

import com.example.Warehouse.domain.enums.Roles;
import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import com.example.Warehouse.services.impl.UserDetailsServiceImpl;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepo;

    public SecurityConfig(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, SecurityContextRepository securityContextRepository) throws Exception {
        http
            .authorizeHttpRequests(
                authorizeHttpRequests ->
                    authorizeHttpRequests.
                        requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                        .permitAll()
                        .requestMatchers("/","/favicon.ico", "/error", "/users/login", "/users/register", "/users/login-error")
                        .permitAll()
                        .requestMatchers("/cart/**", "/orders/**", "/home/admin/**").authenticated()
                        .requestMatchers("/catalog/**", "/warehouses/**", "/home/admin/**", "/orders/admin/**").hasRole(Roles.ADMIN.name())
                        .anyRequest().authenticated()
            )
            .formLogin(
                (formLogin) ->
                    formLogin
                        .loginPage("/users/login")
                        .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                        .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                        .defaultSuccessUrl("/")
                        .failureForwardUrl("/users/login-error")
            ).userDetailsService(userDetailsService())
            .logout((logout) ->
                logout.logoutUrl("/users/logout")
                    .logoutSuccessUrl("/")
                    .invalidateHttpSession(true)
            ).securityContext(
                securityContext -> securityContext
                    .securityContextRepository(securityContextRepository)
            ).rememberMe(
                rememberMe -> rememberMe
                    .key("encription key")
                    .rememberMeParameter("remember")
                    .tokenValiditySeconds(60 * 60 * 244 * 14)
                    .userDetailsService(userDetailsService())
                    .alwaysRemember(true)
            );

        return http.build();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new DelegatingSecurityContextRepository(
            new RequestAttributeSecurityContextRepository(),
            new HttpSessionSecurityContextRepository()
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl(userRepo);
    }
}
