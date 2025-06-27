package com.example.demo.DTO.Producto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EstadisticasProductosResponse {
    private String nombreProducto;
    private long cantidadVendida;
    private double totalGenerado;
}
