package com.example.demo.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.demo.DTO.MovimientoInventarioDTO;
import com.example.demo.DTO.MovimientoInventarioResponseDTO;
import com.example.demo.Models.MovimientoInventario;
import com.example.demo.Models.Producto;
import com.example.demo.Models.TipoMovimiento;
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

public MovimientoInventarioResponseDTO registrarMovimiento(MovimientoInventarioDTO dto) {
    Producto producto = productoRepo.findById(dto.getProductoId())
        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

    MovimientoInventario movimiento = new MovimientoInventario();
    movimiento.setProducto(producto);
    movimiento.setCantidad(dto.getCantidad());
    movimiento.setUbicacion(dto.getUbicacion());
    movimiento.setObservacion(dto.getObservacion());
    movimiento.setTipo(dto.getTipo());
    movimiento.setFecha(dto.getFecha());

    MovimientoInventario guardado = movimientoRepo.save(movimiento);

    // Mapear a DTO de respuesta con nombre y categoría del producto
    MovimientoInventarioResponseDTO response = new MovimientoInventarioResponseDTO();
    response.setProductoNombre(producto.getNombre());
    response.setCategoria(producto.getCategoria());
    response.setCantidad(guardado.getCantidad());
    response.setUbicacion(guardado.getUbicacion());
    response.setObservacion(guardado.getObservacion());
    response.setTipo(guardado.getTipo());
    response.setFecha(guardado.getFecha());

    return response;
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
    public List<MovimientoInventarioResponseDTO> filtrarPorCategoria(String categoria) {
        return movimientoRepo.findAll().stream()
                .filter(m -> m.getProducto().getCategoria().equalsIgnoreCase(categoria))
                .map(m -> {
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
    public List<Producto> listarStockBajo() {
        return productoRepo.findAll().stream()
                .filter(p -> p.getStock() < 10)
                .collect(Collectors.toList());
    }

    @Override
    public List<Producto> listarProximosVencimientos() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusDays(7);

        return productoRepo.findAll().stream()
                .filter(p -> p.getFechaVencimiento() != null)
                .filter(p -> !p.getFechaVencimiento().isBefore(hoy) && !p.getFechaVencimiento().isAfter(limite))
                .collect(Collectors.toList());
    }

    @Override
public List<MovimientoInventarioResponseDTO> filtrarPorTipo(TipoMovimiento tipo) {
    return movimientoRepo.findAll().stream()
            .filter(m -> m.getTipo() == tipo)
            .map(m -> {
                MovimientoInventarioResponseDTO dto = new MovimientoInventarioResponseDTO();
                dto.setProductoNombre(m.getProducto().getNombre());
                dto.setCategoria(m.getProducto().getCategoria());
                dto.setCantidad(m.getCantidad());
                dto.setUbicacion(m.getUbicacion());
                dto.setObservacion(m.getObservacion());
                dto.setTipo(m.getTipo());
                dto.setFecha(m.getFecha());
                return dto;
            })
            .collect(Collectors.toList());
}

}
