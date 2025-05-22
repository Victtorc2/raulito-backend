package com.example.demo.Services;


import com.example.demo.Models.Producto;


import java.util.List;
import java.util.Optional;

public interface ProductoService {
    List<Producto> listarProductos();
    Optional<Producto> obtenerProductoPorId(Long id);
    Producto crearProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);
    void eliminarProducto(Long id);
    List<Producto> buscarPorNombre(String nombre);
List<Producto> buscarPorCategoria(String categoria);
List<Producto> buscarPorCodigo(String codigo);

}