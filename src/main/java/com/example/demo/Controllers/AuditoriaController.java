package com.example.demo.Controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Models.Auditoria;
import com.example.demo.Repositories.AuditoriaRepository;

@RestController
@RequestMapping("/api/auditoria")
public class AuditoriaController {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @GetMapping
    public ResponseEntity<List<Auditoria>> obtenerRegistrosAuditoria() {
        List<Auditoria> registros = auditoriaRepository.findAll();
        return ResponseEntity.ok(registros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> obtenerRegistroAuditoriaPorId(@PathVariable Long id) {
        return auditoriaRepository.findById(id)
                .map(auditoria -> ResponseEntity.ok(auditoria))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}

