package com.example.demo.Services;

import com.example.demo.Models.Producto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface IProductoService {
    List<Producto> listarProductos();

    Optional<Producto> obtenerProductoPorId(Long id);

    Producto crearProducto(Producto producto);

    Producto actualizarProducto(Long id, Producto producto);

    void eliminarProducto(Long id);

    List<Producto> buscarPorNombre(String nombre);

    List<Producto> buscarPorCategoria(String categoria);

    List<Producto> buscarPorCodigo(String codigo);

    List<Producto> listarProductosProximosAVencer(int dias);

    List<Producto> listarProductosStockBajo(int stockMinimo);

    List<Producto> listarConFiltros(String nombre, String categoria, String codigo);

    ResponseEntity<?> crearProductoDesdeJson(String productoJson, MultipartFile imagen, String usuario);

    ResponseEntity<?> actualizarProductoDesdeJson(Long id, String productoJson, MultipartFile imagen, String usuario);

    ResponseEntity<Void> eliminarProductoConAuditoria(Long id, String usuario);

    ResponseEntity<byte[]> obtenerImagenProducto(Long id);
}
