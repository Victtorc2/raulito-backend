package com.example.demo.Controllers;

import com.example.demo.DTO.Auth.AuthRequest;
import com.example.demo.DTO.Auth.AuthResponse;
import com.example.demo.Models.Usuario;
import com.example.demo.Services.IAuditoriaService;
import com.example.demo.Services.IUsuarioService;
import com.example.demo.Util.JWTUtil;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService; // ✅
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final IUsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtService;
    private final UserDetailsService userDetailsService; // ✅ Cambiado aquí
    private final IAuditoriaService auditoriaService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Intentando autenticar usuario {}", request.getCorreo());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCorreo(), request.getPassword()));
            logger.info("Autenticación exitosa");
        } catch (BadCredentialsException e) {
            logger.warn("Credenciales inválidas");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    new AuthResponse(null, null, "Credenciales inválidas"));
        } catch (DisabledException e) {
            logger.warn("Cuenta deshabilitada");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new AuthResponse(null, null, "La cuenta está deshabilitada"));
        } catch (LockedException e) {
            logger.warn("Cuenta bloqueada");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new AuthResponse(null, null, "La cuenta está bloqueada"));
        } catch (AccountExpiredException e) {
            logger.warn("Cuenta expirada");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new AuthResponse(null, null, "La cuenta ha expirado"));
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
        Usuario usuario = usuarioService.obtenerPorCorreo(request.getCorreo())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtService.generateToken(userDetails, usuario);

        auditoriaService.registrarAuditoria(
                request.getCorreo(),
                "auth",
                "login",
                "Inicio de sesión exitoso",
                null,
                "Token generado");

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .map(auth -> auth.replace("ROLE_", ""))
                .orElse("");

        return ResponseEntity.ok(new AuthResponse(token, role, usuario.getCorreo()));
    }
}
