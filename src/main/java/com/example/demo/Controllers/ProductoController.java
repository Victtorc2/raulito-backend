package com.example.demo.Controllers;

import com.example.demo.Models.Producto;
import com.example.demo.Services.IProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService;

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Producto> obtenerProducto(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") String productoJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestHeader("usuario") String usuario) {
        return productoService.crearProductoDesdeJson(productoJson, imagen, usuario);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProducto(
            @PathVariable Long id,
            @RequestPart("producto") String productoJson,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen,
            @RequestHeader("usuario") String usuario) {
        return productoService.actualizarProductoDesdeJson(id, productoJson, imagen, usuario);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarProducto(
            @PathVariable Long id,
            @RequestHeader("usuario") String usuario) {
        return productoService.eliminarProductoConAuditoria(id, usuario);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public List<Producto> listarProductos(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String codigo) {
        return productoService.listarConFiltros(nombre, categoria, codigo);
    }

    @GetMapping("/alertas/vencimiento")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> productosProximosAVencer(
            @RequestParam(defaultValue = "7") int dias) {
        return productoService.listarProductosProximosAVencer(dias);
    }

    @GetMapping("/alertas/stock-bajo")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> productosStockBajo(
            @RequestParam(defaultValue = "10") int stockMinimo) {
        return productoService.listarProductosStockBajo(stockMinimo);
    }

    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long id) {
        return productoService.obtenerImagenProducto(id);
    }
}
