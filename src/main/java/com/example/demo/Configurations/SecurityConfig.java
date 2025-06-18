package com.example.demo.Configurations;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.demo.Services.*;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
            .cors()
            .and()
            .csrf(csrf -> csrf.disable())  // Desactiva CSRF si no usas cookies de sesión
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Primero permitir todas las OPTIONS (preflight)
                    .requestMatchers("/auth/login").permitAll()  // Permitir acceso al login sin autenticación
                    .requestMatchers("/public/**").permitAll()  // Permitir acceso a los recursos públicos
                    .requestMatchers("/api/productos/*/imagen").permitAll()  // Permitir imágenes de productos sin autenticación
                    .requestMatchers("/api/productos/**").hasAnyRole("EMPLEADO", "ADMIN")  // Solo EMPLEADO o ADMIN pueden acceder a productos
                    .requestMatchers("/api/usuarios/**").hasRole("ADMIN")  // Solo ADMIN puede acceder a usuarios
                    .requestMatchers("/api/inventario/**").hasAnyRole("ADMIN", "EMPLEADO")  // ADMIN y EMPLEADO pueden acceder a inventario
                    .requestMatchers("/api/ventas/**").hasAnyRole("ADMIN", "EMPLEADO")  // ADMIN y EMPLEADO pueden acceder a ventas
                    .anyRequest().authenticated())  // Cualquier otra solicitud debe estar autenticada
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))  // Sin sesión (usamos JWT)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);  // Filtro para JWT
    return http.build();
}


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // Asegúrate de que este es el puerto de tu frontend
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
@Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
