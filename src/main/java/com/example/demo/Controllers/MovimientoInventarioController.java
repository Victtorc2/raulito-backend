package com.example.demo.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.MovimientoInventarioResponseDTO;
import com.example.demo.Models.Producto;
import com.example.demo.Services.MovimientoInventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class MovimientoInventarioController {

    @Autowired
    private MovimientoInventarioService movimientoInventarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<?> registrarMovimiento(@RequestBody MovimientoInventarioDTO dto) {
        movimientoInventarioService.registrarMovimiento(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<MovimientoInventarioResponseDTO> listarMovimientos() {
        return movimientoInventarioService.listarMovimientos();
    }

    /*@GetMapping("/categoria/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<MovimientoInventario> filtrarPorCategoria(@PathVariable String nombre) {
        return movimientoInventarioService.filtrarPorCategoria(nombre);
    } */

    @GetMapping("/stock-bajo")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> listarStockBajo() {
        return movimientoInventarioService.listarStockBajo();
    }

    @GetMapping("/proximos-vencimientos")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> listarProximosVencimientos() {
        return movimientoInventarioService.listarProximosVencimientos();
    }
}
