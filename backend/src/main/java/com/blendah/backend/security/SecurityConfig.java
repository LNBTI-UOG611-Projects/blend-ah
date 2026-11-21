package main.java.com.blendah.backend.security;

import com.blendah.backend.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtService jwtService,
            TokenBlocklist tokenBlocklist,
            UserRepository userRepository,
            @Value("${app.security.enforce-admin-roles:false}") boolean enforceAdminRoles) throws Exception {

        http
            .csrf(csrf -> csrf.disable())              // stateless token API, no cookies
            .cors(Customizer.withDefaults())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable())
            .authorizeHttpRequests(auth -> {
                // Always public
                auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                auth.requestMatchers("/error", "/api/health", "/api/health/**").permitAll();
                auth.requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll();

                // Always require a logged-in user (Member 1 + device registration)
                auth.requestMatchers("/api/auth/logout", "/api/users/**", "/api/devices/**").authenticated();

                if (enforceAdminRoles) {
                    auth.requestMatchers("/api/admin/**").hasRole("ADMIN");
                    auth.requestMatchers(HttpMethod.GET, "/api/mini-apps", "/api/mini-apps/**").authenticated();
                    auth.requestMatchers("/api/mini-apps", "/api/mini-apps/**").hasRole("ADMIN");
                    auth.anyRequest().authenticated();
                } else {
                    // Transition mode: keep the admin portal and mini-app endpoints working
                    // exactly as before until the admin portal sends a token.
                    log.warn("ENFORCE_ADMIN_ROLES=false: /api/admin/** and /api/mini-apps/** are NOT protected yet.");
                    auth.anyRequest().permitAll();
                }
            })
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, e) -> {
                    Object reason = request.getAttribute(JwtAuthFilter.AUTH_ERROR_ATTRIBUTE);
                    SecurityErrorWriter.write(response, HttpServletResponse.SC_UNAUTHORIZED,
                            reason != null ? reason.toString() : "Please log in to continue.");
                })
                .accessDeniedHandler((request, response, e) ->
                    SecurityErrorWriter.write(response, HttpServletResponse.SC_FORBIDDEN,
                            "You do not have permission to do this."))
            )
            .addFilterBefore(new JwtAuthFilter(jwtService, tokenBlocklist, userRepository),
                    UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /** Lets Expo web / browser clients call the API. Native apps don't need CORS. */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOriginPatterns(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(false);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}