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
@RequestMapping("/api/auditoria") // ruta

public class AuditoriaController {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @GetMapping //mapea la peticion http get
    public ResponseEntity<List<Auditoria>> obtenerRegistrosAuditoria() {
        List<Auditoria> registros = auditoriaRepository.findAll(); // variable registros que almacenara una lista de objetos de tipo auditoria
        return ResponseEntity.ok(registros); //	Devuelve una respuesta exitosa (200)
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auditoria> obtenerRegistroAuditoriaPorId(@PathVariable Long id) {
        return auditoriaRepository.findById(id)
                .map(auditoria -> ResponseEntity.ok(auditoria))
                //Se usa el método map para verificar si existe una auditoría con ese ID, y si existe, se convierte en una respuesta HTTP 200 OK con el objeto encontrado.”
                .orElseGet(() -> ResponseEntity.notFound().build()); // se ejecuta sino se encontro el objeto   
    }
}

