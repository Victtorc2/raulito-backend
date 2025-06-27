package com.example.demo.Controllers;

import com.example.demo.Models.Auditoria;
import com.example.demo.Services.IAuditoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private IAuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<Auditoria>> obtenerRegistrosAuditoria() {
        return ResponseEntity.ok(auditoriaService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> obtenerRegistroAuditoriaPorId(@PathVariable Long id) {
        return auditoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
