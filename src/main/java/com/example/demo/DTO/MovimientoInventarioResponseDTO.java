package com.example.demo.DTO;


import java.time.LocalDate;

import com.example.demo.Models.TipoMovimiento;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovimientoInventarioResponseDTO {
    private String productoNombre; 
    private int precio;

    private String categoria; 
    private int cantidad;
    private String ubicacion;
    private String observacion;
    private TipoMovimiento tipo;
    private LocalDate fecha;
}