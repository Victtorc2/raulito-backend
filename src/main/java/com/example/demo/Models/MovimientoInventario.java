package com.example.demo.Models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoInventario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    private int cantidad;
    private String ubicacion;
    private String observacion;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipo;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fecha;
}
