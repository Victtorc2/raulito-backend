package com.example.demo.DTO.Venta;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequest {
    private Long usuarioId;
    private List<ItemVentaDTO> detalles;
    private String metodoPago;
    private double descuento;
}
 