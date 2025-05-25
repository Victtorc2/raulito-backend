package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.EstadisticasVentaDTO;
import com.example.demo.DTO.VentaDiariaDTO;
import com.example.demo.DTO.VentaMensualDTO;
import com.example.demo.DTO.VentaRequestDTO;
import com.example.demo.DTO.VentaResponseDTO;
import com.example.demo.Services.VentaService;

@RestController
@RequestMapping("/api/ventas")
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<VentaResponseDTO> registrarVenta(@RequestBody VentaRequestDTO ventaDTO) {
        VentaResponseDTO nuevaVenta = ventaService.registrarVenta(ventaDTO);
        return ResponseEntity.ok(nuevaVenta);
    }

    @GetMapping("/historial")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLEADO')")
    public ResponseEntity<List<VentaResponseDTO>> obtenerHistorial() {
        List<VentaResponseDTO> historial = ventaService.obtenerTodasLasVentas();
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
