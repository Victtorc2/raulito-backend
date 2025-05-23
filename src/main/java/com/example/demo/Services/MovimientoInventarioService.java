package com.example.demo.Services;

import java.util.List;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.MovimientoInventarioResponseDTO;
import com.example.demo.Models.Producto;
import com.example.demo.Models.TipoMovimiento;

public interface MovimientoInventarioService {
    MovimientoInventarioResponseDTO registrarMovimiento(MovimientoInventarioDTO dto);

    List<MovimientoInventarioResponseDTO> listarMovimientos();

    List<MovimientoInventarioResponseDTO> filtrarPorCategoria(String categoria);

    List<Producto> listarStockBajo();

    List<MovimientoInventarioResponseDTO> filtrarPorTipo(TipoMovimiento tipo);

    List<Producto> listarProximosVencimientos();
}
