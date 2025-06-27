package com.example.demo.DTO.Venta;

import lombok.Data;
import java.util.Map;

import com.example.demo.DTO.Producto.EstadisticasProductosResponse;

import java.util.List;

@Data
public class EstadisticasVentaDTO {
    private long ventasHoy;
    private double ingresosHoy;
    private long ventasMes;
    private double ingresosMes;

    private Map<String, Long> ventasPorMetodoPago;

    // producto → [cantidadVendida, totalGenerado]
    private List<EstadisticasProductosResponse> productosMasVendidos;
}

