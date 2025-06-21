package com.example.demo.Models;

import java.time.LocalDate; //atributo de tipo LocalDate para fechas

import org.springframework.format.annotation.DateTimeFormat; //para formatear la fecha

import jakarta.persistence.Column;
import jakarta.persistence.Entity; //importa la clase Column de JPA
import jakarta.persistence.GeneratedValue; //importa la clase Entity de JPA
import jakarta.persistence.GenerationType; //importa la clase GeneratedValue de JPA
import jakarta.persistence.Id; //importa la clase GenerationType de JPA
import jakarta.persistence.Lob; //importa la clase Id de JPA
import lombok.AllArgsConstructor;
import lombok.Data; //genera constructor con todos los atributos
import lombok.NoArgsConstructor; //genera getters y setters

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
@Lob

@Column(name = "imagen", columnDefinition = "LONGBLOB")
    private byte[] imagen;
    private String imagenUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaVencimiento;

}
