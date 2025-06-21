package com.example.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Models.Auditoria;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
    // Puedes agregar consultas personalizadas aquí si es necesario.
}
