package com.example.demo.DTO;


import java.time.LocalDate;

import com.example.demo.Models.TipoMovimiento;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class MovimientoInventarioResponseDTO {
    private String productoNombre; // para mostrar el nombre
    private String categoria; // <-- agrega esto

    private int cantidad;
    private String ubicacion;
    private String observacion;
    private TipoMovimiento tipo;
    private LocalDate fecha;
}