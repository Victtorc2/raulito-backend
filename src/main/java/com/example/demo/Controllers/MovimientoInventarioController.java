package com.example.demo.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.MovimientoInventario.MovimientoInventarioRequest;
import com.example.demo.DTO.MovimientoInventario.MovimientoInventarioResponse;
import com.example.demo.Models.Producto;
import com.example.demo.Models.TipoMovimiento;
import com.example.demo.Services.IMovimientoInventarioService;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
public class MovimientoInventarioController {

    @Autowired
    private IMovimientoInventarioService movimientoInventarioService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<MovimientoInventarioResponse> registrarMovimiento(
            @RequestBody MovimientoInventarioRequest dto) {
        MovimientoInventarioResponse response = movimientoInventarioService.registrarMovimiento(dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<MovimientoInventarioResponse> listarMovimientos() {
        return movimientoInventarioService.listarMovimientos();
    }

    @GetMapping("/categoria/{nombre}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<MovimientoInventarioResponse> filtrarPorCategoria(@PathVariable String nombre) {
        return movimientoInventarioService.filtrarPorCategoria(nombre);
    }

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

    @GetMapping("/tipo/{tipo}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<MovimientoInventarioResponse> filtrarPorTipo(@PathVariable TipoMovimiento tipo) {
        return movimientoInventarioService.filtrarPorTipo(tipo);
    }

}
