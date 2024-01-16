package ticketbooking.Ticket.Booking.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JWTAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.csrf(csrf -> csrf.disable());
        httpSecurity.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers("/api/register", "/api/login", "/api/logout").permitAll()
                        .requestMatchers("/api/buser/*", "/api/buser/*/*").hasAuthority("ROLE_BUSER")
                        .requestMatchers("/api/admin/*", "/api/admin/**").hasAuthority("ROLE_ADMIN")
                        .anyRequest().authenticated()
        );
        httpSecurity.authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
//        httpSecurity.logout(
//                logout ->logout.invalidateHttpSession(true)
//                        .clearAuthentication(true)
//                        .logoutUrl("/api/logout")
//                        .addLogoutHandler((request, response, auth) -> {
//                            try {
//                                request.logout();
//                            } catch (ServletException e) {
//                                 System.out.println(e.getMessage());
//                            }
//                        }
//        ));
        return httpSecurity.build();
    }
}
