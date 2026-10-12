package com.itvedant.theatre.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import com.itvedant.theatre.services.CustomUserDetailsService;

@Configuration
public class SecurityConfiguration {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfiguration(
            CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder());

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .authenticationProvider(authenticationProvider())
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/auth/register",
                    "/error"
                ).permitAll()

                .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/movies")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/movies/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/movies/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/screens")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/screens/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/screens/**")
                    .hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/api/theatres")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/theatres/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/theatres/**")
                    .hasRole("ADMIN")

                .anyRequest().authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(form -> form
                .usernameParameter("username")
                .defaultSuccessUrl("/api/auth/welcome", true)
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login?logout")
                .permitAll()
            );

        return http.build();
    }
}