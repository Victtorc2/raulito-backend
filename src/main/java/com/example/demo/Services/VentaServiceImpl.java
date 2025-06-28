package com.example.demo.Services;

import com.example.demo.DTO.*;
import com.example.demo.Models.*;
import com.example.demo.Repositories.*;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoInventarioRepository movimientoRepo;

    @Override
@Transactional
public VentaResponseDTO registrarVenta(VentaRequestDTO ventaDTO) {
    // 1️⃣ Validar que venga el correo
    if (ventaDTO.getCorreo() == null || ventaDTO.getCorreo().isBlank()) {
    throw new IllegalArgumentException("El correo del usuario no puede ser null o vacío");
}

// 2. Buscar al usuario en la BD usando el correo
Usuario usuario = usuarioRepository
    .findByCorreo(ventaDTO.getCorreo())
    .orElseThrow(() -> new RuntimeException(
        "Usuario no encontrado con correo: " + ventaDTO.getCorreo()));

    Venta venta = new Venta();
    venta.setFecha(LocalDateTime.now());
    venta.setUsuario(usuario);
    venta.setMetodoPago(ventaDTO.getMetodoPago());

    List<DetalleVenta> detalles = new ArrayList<>();
    double total = 0;

    for (ItemVentaDTO item : ventaDTO.getDetalles()) {
        Producto producto = productoRepository.findById(item.getProductoId())
            .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < item.getCantidad()) {
            throw new RuntimeException("Stock insuficiente para el producto: " + producto.getNombre());
        }

        double precio   = producto.getPrecio();
        double subtotal = precio * item.getCantidad();
        total += subtotal;

        producto.setStock(producto.getStock() - item.getCantidad());
        productoRepository.save(producto);

        DetalleVenta detalle = new DetalleVenta();
        detalle.setVenta(venta);
        detalle.setProducto(producto);
        detalle.setCantidad(item.getCantidad());
        detalle.setPrecioUnitario(precio);
        detalle.setSubtotal(subtotal);
        detalles.add(detalle);

        MovimientoInventario movimiento = new MovimientoInventario();
        movimiento.setProducto(producto);
        movimiento.setCantidad(item.getCantidad());
        movimiento.setUbicacion("VENTA");
        movimiento.setObservacion("Salida por venta realizada por: " + usuario.getCorreo());
        movimiento.setTipo(TipoMovimiento.SALIDA);
        movimiento.setFecha(LocalDate.now());
        movimiento.setPrecio(precio);
        movimientoRepo.save(movimiento);
    }

    venta.setTotal(total);
    venta.setDetalles(detalles);
    Venta ventaGuardada = ventaRepository.save(venta);

    return mapToResponseDTO(ventaGuardada);
}

    private VentaResponseDTO mapToResponseDTO(Venta venta) {
        VentaResponseDTO dto = new VentaResponseDTO();
        dto.setId(venta.getId());
        dto.setFecha(venta.getFecha().toLocalDate());
        dto.setHora(venta.getFecha().toLocalTime());
        dto.setUsuarioCorreo(venta.getUsuario().getCorreo());
        dto.setTotal(venta.getTotal());
        dto.setMetodoPago(venta.getMetodoPago());

        List<DetalleVentaDTO> detallesDTO = venta.getDetalles().stream().map(det -> {
            DetalleVentaDTO d = new DetalleVentaDTO();
            d.setProductoNombre(det.getProducto().getNombre());
            d.setCantidad(det.getCantidad());
            d.setPrecioUnitario(det.getPrecioUnitario());
            d.setSubtotal(det.getSubtotal());
            return d;
        }).toList();

        dto.setDetalles(detallesDTO);
        return dto;
    }

    public EstadisticasVentaDTO obtenerEstadisticas() {
        EstadisticasVentaDTO dto = new EstadisticasVentaDTO();

        LocalDateTime inicio = LocalDate.now().atStartOfDay();
        LocalDateTime fin = inicio.plusDays(1);

        dto.setVentasHoy(ventaRepository.contarVentasHoy(inicio, fin));
        dto.setIngresosHoy(ventaRepository.obtenerIngresosHoy(inicio, fin));

        dto.setVentasMes(ventaRepository.contarVentasMes());
        dto.setIngresosMes(ventaRepository.obtenerIngresosMes());

        Map<String, Long> metodoMap = new HashMap<>();
        for (Object[] row : ventaRepository.contarPorMetodoPago()) {
            metodoMap.put((String) row[0], ((Number) row[1]).longValue());
        }
        dto.setVentasPorMetodoPago(metodoMap);

        List<ProductoEstadisticaDTO> productos = ventaRepository.productosMasVendidos().stream()
                .map(row -> new ProductoEstadisticaDTO(
                        (String) row[0],
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).doubleValue()))
                .toList();
        dto.setProductosMasVendidos(productos);

        return dto;
    }

    @Override
    public List<VentaResponseDTO> obtenerTodasLasVentas() {
        List<Venta> ventas = ventaRepository.findAllByOrderByFechaDesc();
        return ventas.stream().map(this::mapToResponseDTO).toList();
    }

    @Override
    public List<VentaDiariaDTO> obtenerVentasPorDia() {
        return ventaRepository.obtenerVentasPorDia().stream()
                .map(row -> new VentaDiariaDTO(
                        ((java.sql.Date) row[0]).toLocalDate(),
                        ((Number) row[1]).doubleValue()))
                .toList();
    }

    @Override
    public List<VentaMensualDTO> obtenerVentasPorMes() {
        return ventaRepository.obtenerVentasPorMes().stream()
                .map(row -> new VentaMensualDTO(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue(),
                        ((Number) row[2]).doubleValue()))
                .toList();
    }

}

