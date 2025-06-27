package com.example.demo.DTO.Producto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProductoRequest {
    private Long id;
    private String nombre;
    private String codigo;
    private String categoria;
    private Double precio;
    private int stock;
    private String proveedor;
    private String presentacion;
    private LocalDate fechaVencimiento;

}
