package com.example.demo.DTO;

import com.example.demo.Models.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private Integer dni;
    private String correo;
    private Set<Rol> roles;
}
