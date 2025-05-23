package com.example.demo.Repositories;


import com.example.demo.Models.Producto;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    List<Producto> findByCategoriaIgnoreCase(String categoria);
    
    List<Producto> findByCodigoIgnoreCase(String codigo);
    Optional<Producto> findByNombre(String nombre);

    }
