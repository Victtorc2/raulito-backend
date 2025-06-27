package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.Venta.EstadisticasVentaDTO;
import com.example.demo.DTO.Venta.VentaDiariaDTO;
import com.example.demo.DTO.Venta.VentaMensualDTO;
import com.example.demo.DTO.Venta.VentaRequest;
import com.example.demo.DTO.Venta.VentaResponse;
import com.example.demo.Services.IAuditoriaService;
import com.example.demo.Services.IVentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;
    @Autowired
    private IAuditoriaService auditoriaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<VentaResponse> registrarVenta(
            @RequestBody VentaRequest ventaDTO,
            @RequestHeader("usuario") String usuario) {

        VentaResponse nuevaVenta = ventaService.registrarVenta(ventaDTO);

        auditoriaService.registrarAuditoria(usuario, "ventas", "registrar",
                "Venta registrada. Monto: " + nuevaVenta.getTotal(), null, ventaDTO.toString());

        return ResponseEntity.ok(nuevaVenta);
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<VentaResponse>> obtenerHistorial() {
        List<VentaResponse> historial = ventaService.obtenerTodasLasVentas();
        return ResponseEntity.ok(historial);
    }

    @GetMapping("/estadisticas")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<EstadisticasVentaDTO> obtenerEstadisticas() {
        EstadisticasVentaDTO estadisticas = ventaService.obtenerEstadisticas();
        return ResponseEntity.ok(estadisticas);
    }

    @GetMapping("/reporte/por-dia")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<VentaDiariaDTO>> obtenerReportePorDia() {
        return ResponseEntity.ok(ventaService.obtenerVentasPorDia());
    }

    @GetMapping("/reporte/por-mes")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<VentaMensualDTO>> obtenerReportePorMes() {
        return ResponseEntity.ok(ventaService.obtenerVentasPorMes());
    }

}
