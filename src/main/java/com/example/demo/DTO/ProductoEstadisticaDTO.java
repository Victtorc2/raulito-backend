package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoEstadisticaDTO {
    private String nombreProducto;
    private long cantidadVendida;
    private double totalGenerado;
}
