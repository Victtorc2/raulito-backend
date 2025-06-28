package com.example.demo.DTO;

import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private String correo;
    private List<ItemVentaDTO> detalles;
    private String metodoPago;
    private double descuento;
}