package com.example.demo.Controllers;

import com.example.demo.DTO.ProductoDTO;
import com.example.demo.Models.Producto;
import com.example.demo.Services.ProductoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.io.IOException;

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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearProducto(
            @RequestPart("producto") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        Producto producto = new Producto();
        producto.setNombre(productoDTO.getNombre());
        producto.setCodigo(productoDTO.getCodigo());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());
        producto.setStock(productoDTO.getStock());
        producto.setProveedor(productoDTO.getProveedor());
        producto.setPresentacion(productoDTO.getPresentacion());
        producto.setFechaVencimiento(productoDTO.getFechaVencimiento());

        if (imagen != null && !imagen.isEmpty()) {
            producto.setImagen(imagen.getBytes());
        }

        Producto nuevo = productoService.crearProducto(producto);
        return ResponseEntity.ok(nuevo);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> actualizarProductoConImagen(
            @PathVariable Long id,
            @RequestPart("producto") ProductoDTO productoDTO,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {

        Producto producto = productoService.obtenerProductoPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setNombre(productoDTO.getNombre());
        producto.setCodigo(productoDTO.getCodigo());
        producto.setCategoria(productoDTO.getCategoria());
        producto.setPrecio(productoDTO.getPrecio());

        producto.setProveedor(productoDTO.getProveedor());
        producto.setPresentacion(productoDTO.getPresentacion());
        producto.setFechaVencimiento(productoDTO.getFechaVencimiento());

        if (imagen != null && !imagen.isEmpty()) {
            producto.setImagen(imagen.getBytes());
        }

        Producto actualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(actualizado);
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

    @GetMapping("/alertas/vencimiento")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> productosProximosAVencer(@RequestParam(defaultValue = "7") int dias) {
        return productoService.listarProductosProximosAVencer(dias);
    }

    @GetMapping("/alertas/stock-bajo")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Producto> productosStockBajo(@RequestParam(defaultValue = "10") int stockMinimo) {
        return productoService.listarProductosStockBajo(stockMinimo);
    }

    // ✅ NUEVO: Endpoint para obtener imagen como recurso
    @GetMapping("/{id}/imagen")
    public ResponseEntity<byte[]> obtenerImagenProducto(@PathVariable Long id) {
        Optional<Producto> productoOpt = productoService.obtenerProductoPorId(id);

        if (productoOpt.isEmpty() || productoOpt.get().getImagen() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imagenBytes = productoOpt.get().getImagen();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
    }
}
