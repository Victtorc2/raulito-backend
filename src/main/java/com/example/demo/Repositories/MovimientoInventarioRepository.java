package com.example.demo.Repositories;


import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.MovimientoInventario;
import com.example.demo.Models.TipoMovimiento;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
  // Buscar movimientos por categoría del producto (insensible a mayúsculas)
    List<MovimientoInventario> findByProducto_CategoriaIgnoreCase(String categoria);

    // Buscar movimientos entre fechas
    List<MovimientoInventario> findByFechaBetween(LocalDate inicio, LocalDate fin);

    // Buscar movimientos por tipo
    List<MovimientoInventario> findByTipo(TipoMovimiento tipo);

    // Buscar movimientos por id de producto
    List<MovimientoInventario> findByProducto_Id(Long productoId);

    // Buscar movimientos por nombre de producto (insensible a mayúsculas)
    List<MovimientoInventario> findByProducto_NombreIgnoreCase(String nombre);
    
}