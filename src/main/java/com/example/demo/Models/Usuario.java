package com.example.demo.Models;

import jakarta.persistence.*; //importa la clase Entity de JPA
import lombok.AllArgsConstructor; //genera constructor con todos los atributos
import lombok.Data; //genera getters y setters
import lombok.NoArgsConstructor; //genera constructor sin atributos
import jakarta.validation.constraints.*; //importa las clases de validacion de JPA

import java.util.HashSet; //importa la clase HashSet de Java para la encriptacion
import java.util.Set; //importa la clase Set de Java para la encriptacion


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario { //es una entidad de JPA

    @Id //es la clave primaria que se va a generar automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se genera automaticamente el id
    private Long id; //clave primaria tipo Long

    @NotBlank(message = "El nombre no puede estar vacío") //validacion de que no sea nulo
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres")
    @Column(name = "nombre") //nombre de la columna en la base de datos 
    private String nombre;

    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 2, max = 50, message = "El apellido debe tener entre 2 y 50 caracteres")
    @Column(name = "apellido")
    private String apellido;

    @NotNull(message = "El DNI no puede ser nulo")
    @Column(name = "dni", unique = true)
    private Integer dni;

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "Debe ser un correo válido")
    @Column(name = "correo", unique = true)
    private String correo;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

    @ElementCollection(targetClass = Rol.class, fetch = FetchType.EAGER) //hace que se carguen los roles al mismo tiempo que el usuario
    @CollectionTable(name = "usuarios_roles", joinColumns = @JoinColumn(name = "usuario_id")) //esto hace que se cree una tabla intermedia para los roles
    @Enumerated(EnumType.STRING) // cuando seleccionas un rol lo convierte a string y lo guarda en la base de datos
    @Column(name = "rol") //nombre de la columna en la base de datos
    private Set<Rol> roles = new HashSet<>(); //para la encriptacion de los roles
}
