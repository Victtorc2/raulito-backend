package com.example.demo.DTO;

import lombok.Data;

@Data
public class DetalleVentaDTO {
    private String productoNombre;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
}
