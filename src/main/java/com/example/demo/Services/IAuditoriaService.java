package com.example.demo.Services;

import java.util.List;
import java.util.Optional;

import com.example.demo.Models.Auditoria;

public interface IAuditoriaService {

    void registrarAuditoria(String usuario, String modulo, String accion, String descripcion, Object valorAnterior,
            Object valorNuevo);

    List<Auditoria> obtenerTodos();

    Optional<Auditoria> obtenerPorId(Long id);
}
