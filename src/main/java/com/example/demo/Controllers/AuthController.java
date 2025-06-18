package com.example.demo.Controllers;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.AuthRequest;
import com.example.demo.DTO.AuthResponse;
import com.example.demo.Models.Usuario;
import com.example.demo.Services.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController { // Controlador para manejar la autenticación de usuarios

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); //esta línea crea un logger para registrar eventos en el controlador 
    private final UsuarioService usuarioService;  // Inyecta UsuarioService correctamente

    private final AuthenticationManager authenticationManager; // Inyecta AuthenticationManager para manejar la autenticación
    private final JwtService jwtService; // Inyecta JwtService para generar tokens JWT
    private final CustomUserDetailsService userDetailsService; // Inyecta CustomUserDetailsService para cargar detalles del usuario


    // Método para manejar la solicitud de inicio de sesión

  @PostMapping("/login")
public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getPassword()));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
    Usuario usuario = usuarioService.obtenerPorCorreo(request.getCorreo())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    String token = jwtService.generateToken(userDetails, usuario);

    // Modificación clave: Remover ROLE_ del rol devuelto
    String role = userDetails.getAuthorities().stream()
            .findFirst()
            .map(GrantedAuthority::getAuthority)
            .map(authority -> authority.replace("ROLE_", "")) // Remueve ROLE_
            .orElse("");

    return ResponseEntity.ok(new AuthResponse(token, role));
}
}
