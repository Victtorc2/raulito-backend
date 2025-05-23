package com.example.demo.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.MovimientoInventarioResponseDTO;
import com.example.demo.Models.MovimientoInventario;
import com.example.demo.Models.Producto;
import com.example.demo.Repositories.MovimientoInventarioRepository;
import com.example.demo.Repositories.ProductoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepo;
    private final ProductoRepository productoRepo;

    @Override
    @Transactional
    public MovimientoInventario registrarMovimiento(MovimientoInventarioDTO dto) {
        Producto producto = productoRepo.findByNombre(dto.getProductoNombre())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(dto.getCantidad());
        movimiento.setUbicacion(dto.getUbicacion());
        movimiento.setObservacion(dto.getObservacion());
        movimiento.setTipo(dto.getTipo());
        movimiento.setFecha(dto.getFecha());

        // Ajuste de stock
        switch (dto.getTipo()) {
            case INGRESO -> producto.setStock(producto.getStock() + dto.getCantidad());
            case SALIDA -> producto.setStock(producto.getStock() - dto.getCantidad());
            case AJUSTE -> producto.setStock(dto.getCantidad());
        }

        productoRepo.save(producto);
        return movimientoRepo.save(movimiento);
    }

    @Override
    public List<MovimientoInventarioResponseDTO> listarMovimientos() {
        return movimientoRepo.findAll().stream().map(m -> {
            MovimientoInventarioResponseDTO dto = new MovimientoInventarioResponseDTO();
            dto.setProductoNombre(m.getProducto().getNombre());
            dto.setCategoria(m.getProducto().getCategoria());
            dto.setCantidad(m.getCantidad());
            dto.setUbicacion(m.getUbicacion());
            dto.setObservacion(m.getObservacion());
            dto.setTipo(m.getTipo());
            dto.setFecha(m.getFecha());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<MovimientoInventario> filtrarPorCategoria(String categoria) {
        return movimientoRepo.findAll().stream()
                .filter(m -> m.getProducto().getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarStockBajo() {
        return productoRepo.findAll().stream()
                .filter(p -> p.getStock() < 10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarProximosAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7);

        return productoRepo.findAll().stream()
                .filter(p -> p.getFechaVencimiento() != null)
                .filter(p -> !p.getFechaVencimiento().isBefore(hoy) && !p.getFechaVencimiento().isAfter(limite))
                .collect(Collectors.toList());
    }
}
