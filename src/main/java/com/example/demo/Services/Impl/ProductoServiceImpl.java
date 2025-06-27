package com.example.demo.Services.Impl;

import com.example.demo.DTO.Producto.ProductoRequest;
import com.example.demo.Models.Producto;
import com.example.demo.Repositories.ProductoRepository;
import com.example.demo.Services.IAuditoriaService;
import com.example.demo.Services.IProductoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private IAuditoriaService auditoriaService;
    @Autowired
    private ObjectMapper objectMapper;

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

    @Transactional
    public ResponseEntity<?> crearProductoDesdeJson(String productoJson, MultipartFile imagen, String usuario) {
        try {
            ProductoRequest dto = objectMapper.readValue(productoJson, ProductoRequest.class);

            if (dto.getPrecio() == null || dto.getPrecio() <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El precio es obligatorio y debe ser mayor que 0.");
            }

            Producto producto = new Producto();
            producto.setNombre(dto.getNombre());
            producto.setCodigo(dto.getCodigo());
            producto.setCategoria(dto.getCategoria());
            producto.setPrecio(dto.getPrecio());
            producto.setStock(dto.getStock());
            producto.setProveedor(dto.getProveedor());
            producto.setPresentacion(dto.getPresentacion());
            producto.setFechaVencimiento(dto.getFechaVencimiento());

            if (imagen != null)
                producto.setImagen(imagen.getBytes());

            Producto guardado = productoRepository.save(producto);

            auditoriaService.registrarAuditoria(usuario, "productos", "crear",
                    "Producto creado: " + guardado.getNombre(), null, guardado);

            return ResponseEntity.ok(guardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al crear producto: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> actualizarProductoDesdeJson(Long id, String productoJson, MultipartFile imagen,
            String usuario) {
        try {
            ProductoRequest dto = objectMapper.readValue(productoJson, ProductoRequest.class);
            Producto producto = productoRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            String anterior = producto.toString();

            producto.setNombre(dto.getNombre());
            producto.setCodigo(dto.getCodigo());
            producto.setCategoria(dto.getCategoria());
            producto.setPrecio(dto.getPrecio());
            producto.setProveedor(dto.getProveedor());
            producto.setPresentacion(dto.getPresentacion());
            producto.setFechaVencimiento(dto.getFechaVencimiento());

            if (imagen != null)
                producto.setImagen(imagen.getBytes());

            Producto actualizado = productoRepository.save(producto);

            auditoriaService.registrarAuditoria(usuario, "productos", "actualizar",
                    "Producto actualizado: " + producto.getNombre(), anterior, actualizado.toString());

            return ResponseEntity.ok(actualizado);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Error al actualizar producto: " + e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<Void> eliminarProductoConAuditoria(Long id, String usuario) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty())
            return ResponseEntity.notFound().build();

        Producto producto = productoOpt.get();
        String anterior = producto.toString();

        productoRepository.deleteById(id);

        auditoriaService.registrarAuditoria(usuario, "productos", "eliminar",
                "Producto eliminado: " + producto.getNombre(), anterior, null);

        return ResponseEntity.noContent().build();
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
        LocalDate limite = LocalDate.now().plusDays(dias);
        return productoRepository.findAll().stream()
                .filter(p -> p.getFechaVencimiento() != null)
                .filter(p -> !p.getFechaVencimiento().isBefore(LocalDate.now()))
                .filter(p -> !p.getFechaVencimiento().isAfter(limite))
                .toList();
    }

    @Override
    public List<Producto> listarProductosStockBajo(int stockMinimo) {
        return productoRepository.findAll().stream()
                .filter(p -> p.getStock() < stockMinimo)
                .toList();
    }

    @Override
    public List<Producto> listarConFiltros(String nombre, String categoria, String codigo) {
        if (nombre != null)
            return buscarPorNombre(nombre);
        if (categoria != null)
            return buscarPorCategoria(categoria);
        if (codigo != null)
            return buscarPorCodigo(codigo);
        return listarProductos();
    }

    @Override
    public ResponseEntity<byte[]> obtenerImagenProducto(Long id) {
        Optional<Producto> productoOpt = productoRepository.findById(id);
        if (productoOpt.isEmpty() || productoOpt.get().getImagen() == null) {
            return ResponseEntity.notFound().build();
        }

        byte[] imagenBytes = productoOpt.get().getImagen();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagenBytes, headers, HttpStatus.OK);
    }

    @Override
    public Producto actualizarProducto(Long id, Producto producto) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actualizarProducto'");
    }

    @Override
    public void eliminarProducto(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'eliminarProducto'");
    }
}
