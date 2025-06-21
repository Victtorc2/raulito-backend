package com.example.demo.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Auditoria;
import com.example.demo.Repositories.AuditoriaRepository;

import java.time.LocalDateTime;

@Service
public class AuditoriaService { 

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    /**
     * Registra un evento de auditoría en la base de datos.
     *
     * @param usuario       El usuario que realizó la acción
     * @param modulo        El módulo donde ocurrió la acción (productos, ventas, movimientos)
     * @param accion        El tipo de acción realizada (crear, actualizar, eliminar, etc.)
     * @param descripcion   Descripción de la acción realizada
     * @param valorAnterior Valor anterior antes de la acción (si aplica)
     * @param valorNuevo    Valor nuevo después de la acción (si aplica)
     */
    public void registrarAuditoria(String usuario, String modulo, String accion, String descripcion, String valorAnterior, String valorNuevo) {
        // Crear una nueva instancia de Auditoria con los datos proporcionados
        Auditoria auditoria = new Auditoria();
        auditoria.setUsuario(usuario);
        auditoria.setModulo(modulo);
        auditoria.setAccion(accion);
        auditoria.setDescripcion(descripcion);
        auditoria.setFechaHora(LocalDateTime.now());
        auditoria.setValorAnterior(valorAnterior);
        auditoria.setValorNuevo(valorNuevo);

        // Guardar el registro de auditoría en la base de datos
        auditoriaRepository.save(auditoria);
    }
}
