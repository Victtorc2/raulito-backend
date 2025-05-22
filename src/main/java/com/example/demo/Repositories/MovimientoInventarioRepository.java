package com.example.demo.Repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.Models.MovimientoInventario;

public interface MovimientoInventarioRepository extends JpaRepository<MovimientoInventario, Long> {
        List<MovimientoInventario> findByProducto_CategoriaIgnoreCase(String categoria);

    
}