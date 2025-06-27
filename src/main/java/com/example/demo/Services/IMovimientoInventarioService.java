package com.example.demo.Services;

import java.util.List;

import com.example.demo.DTO.MovimientoInventario.MovimientoInventarioRequest;
import com.example.demo.DTO.MovimientoInventario.MovimientoInventarioResponse;
import com.example.demo.Models.Producto;
import com.example.demo.Models.TipoMovimiento;

public interface IMovimientoInventarioService {
    MovimientoInventarioResponse registrarMovimiento(MovimientoInventarioRequest dto);

    List<MovimientoInventarioResponse> listarMovimientos();

    List<MovimientoInventarioResponse> filtrarPorCategoria(String categoria);

    List<Producto> listarStockBajo();

    List<MovimientoInventarioResponse> filtrarPorTipo(TipoMovimiento tipo);

    List<Producto> listarProximosVencimientos();
}
