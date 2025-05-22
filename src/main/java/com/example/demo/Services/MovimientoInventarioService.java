package com.example.demo.Services;

import java.util.List;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.Models.MovimientoInventario;
import com.example.demo.Models.Producto;

public interface MovimientoInventarioService {
    MovimientoInventario registrarMovimiento(MovimientoInventarioDTO dto);

    List<MovimientoInventario> listarMovimientos();

    List<MovimientoInventario> filtrarPorCategoria(String categoria);

    List<MovimientoInventario> productosConStockBajo();

    List<MovimientoInventario> productosProximosAVencer();

    List<Producto> listarStockBajo();

    List<Producto> listarProximosAVencer();
    List<MovimientoInventario> listarTodos();

    

}