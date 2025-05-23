package com.example.demo.Services;

import java.util.List;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.MovimientoInventarioResponseDTO;
import com.example.demo.Models.MovimientoInventario;
import com.example.demo.Models.Producto;

public interface MovimientoInventarioService {
    MovimientoInventario registrarMovimiento(MovimientoInventarioDTO dto);

    List<MovimientoInventarioResponseDTO> listarMovimientos();

    List<MovimientoInventario> filtrarPorCategoria(String categoria);

    List<Producto> listarStockBajo();

    List<Producto> listarProximosAVencer();
}
