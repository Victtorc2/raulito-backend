package com.example.demo.Controllers;


import com.example.demo.Models.Producto;
import com.example.demo.Services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Producto crearProducto(@RequestBody Producto producto) {
        return productoService.crearProducto(producto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Producto actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoService.actualizarProducto(id, producto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
@PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
public List<Producto> listarProductos(
    @RequestParam(required = false) String nombre,
    @RequestParam(required = false) String categoria,
    @RequestParam(required = false) String codigo) {

    if (nombre != null) {
        return productoService.buscarPorNombre(nombre);
    } else if (categoria != null) {
        return productoService.buscarPorCategoria(categoria);
    } else if (codigo != null) {
        return productoService.buscarPorCodigo(codigo);
    } else {
        return productoService.listarProductos();
    }
}

}
