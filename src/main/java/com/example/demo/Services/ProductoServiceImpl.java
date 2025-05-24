package com.example.demo.Services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
            p.setPrecio(producto.getPrecio()); 
            // NO actualices el stock aquí para evitar sobrescribir movimientos
            // p.setStock(producto.getStock()); <- comenta o elimina esta línea
            p.setProveedor(producto.getProveedor());
            p.setPresentacion(producto.getPresentacion());
            if (producto.getImagen() != null) {
                p.setImagen(producto.getImagen());
            }
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

    @Override
    public List<Producto> listarProductosProximosAVencer(int dias) {
        LocalDate fechaLimite = LocalDate.now().plusDays(dias);
        return productoRepository.findAll().stream()
                .filter(p -> p.getFechaVencimiento() != null && !p.getFechaVencimiento().isBefore(LocalDate.now()))
                .filter(p -> !p.getFechaVencimiento().isAfter(fechaLimite))
                .toList();
    }

    @Override
    public List<Producto> listarProductosStockBajo(int stockMinimo) {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() < stockMinimo)
                .toList();
    }

}