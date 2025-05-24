package com.example.demo.Services;

import com.example.demo.DTO.EstadisticasVentaDTO;
import com.example.demo.DTO.VentaRequestDTO;
import com.example.demo.DTO.VentaResponseDTO;

import java.util.List;
import com.example.demo.DTO.VentaDiariaDTO;
import com.example.demo.DTO.VentaMensualDTO;

public interface VentaService {
    VentaResponseDTO registrarVenta(VentaRequestDTO ventaDTO);

    EstadisticasVentaDTO obtenerEstadisticas();

    List<VentaResponseDTO> obtenerTodasLasVentas();

    List<VentaDiariaDTO> obtenerVentasPorDia();

    List<VentaMensualDTO> obtenerVentasPorMes();

}
