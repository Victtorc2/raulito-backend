package com.example.demo.Services;

import com.example.demo.DTO.Venta.EstadisticasVentaDTO;
import com.example.demo.DTO.Venta.VentaDiariaDTO;
import com.example.demo.DTO.Venta.VentaMensualDTO;
import com.example.demo.DTO.Venta.VentaRequest;
import com.example.demo.DTO.Venta.VentaResponse;

import java.util.List;

public interface IVentaService {
    VentaResponse registrarVenta(VentaRequest ventaDTO);

    EstadisticasVentaDTO obtenerEstadisticas();

    List<VentaResponse> obtenerTodasLasVentas();

    List<VentaDiariaDTO> obtenerVentasPorDia();

    List<VentaMensualDTO> obtenerVentasPorMes();

}
