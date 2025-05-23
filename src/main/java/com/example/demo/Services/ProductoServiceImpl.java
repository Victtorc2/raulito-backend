package com.example.demo.Services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Producto;
import com.example.demo.Repositories.ProductoRepository;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    @Override
    public Producto crearProducto(Producto producto) {
        return productoRepository.save(producto);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        return productoRepository.findById(id).map(p -> {
            p.setNombre(producto.getNombre());
            p.setCodigo(producto.getCodigo());
            p.setCategoria(producto.getCategoria());
            p.setStock(producto.getStock());
            p.setProveedor(producto.getProveedor());
            p.setPresentacion(producto.getPresentacion());
            p.setImagen(producto.getImagen());
            p.setFechaVencimiento(producto.getFechaVencimiento());
            return productoRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @Override
    public void eliminarProducto(Long id) {
        productoRepository.deleteById(id);
    }
    @Override
public List<Producto> buscarPorNombre(String nombre) {
    return productoRepository.findByNombreContainingIgnoreCase(nombre);
}

@Override
public List<Producto> buscarPorCategoria(String categoria) {
    return productoRepository.findByCategoriaIgnoreCase(categoria);
}

@Override
public List<Producto> buscarPorCodigo(String codigo) {
    return productoRepository.findByCodigoIgnoreCase(codigo);
}






}