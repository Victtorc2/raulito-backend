package com.example.demo.Models;

import java.time.LocalDate; //atributo de tipo LocalDate para fechas

import org.springframework.format.annotation.DateTimeFormat; //para formatear la fecha


//mapean la clase a una tabla de la base de datos
import jakarta.persistence.Column; //importa la clase Column de JPA
import jakarta.persistence.Entity; //importa la clase Entity de JPA
import jakarta.persistence.GeneratedValue; //importa la clase GeneratedValue de JPA
import jakarta.persistence.GenerationType; //importa la clase GenerationType de JPA
import jakarta.persistence.Id; //importa la clase Id de JPA
import lombok.AllArgsConstructor; //genera constructor con todos los atributos
import lombok.Data; //genera getters y setters
import lombok.NoArgsConstructor; //genera constructor sin atributos

@Entity //es una entidad de JPA
@Data //genera getters y setters
@AllArgsConstructor //genera constructor con todos los atributos
@NoArgsConstructor //genera constructor sin atributos

public class Producto {  //es una entidad de JPA
    @Id //es la clave primaria que se va a generar automaticamente
    @GeneratedValue(strategy = GenerationType.IDENTITY) //se genera automaticamente el id

    private long id; 
    private String nombre; 

    @Column(nullable = false)
    private Double precio;

    private String codigo;
    private String categoria;

    @Column(name = "stock")
    private int stock;

    private String proveedor;
    private String presentacion;

    @Column(name = "imagen")
    private byte[] imagen;
    private String imagenUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

}
