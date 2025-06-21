package com.example.demo.Services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Models.Auditoria;
import com.example.demo.Repositories.AuditoriaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

@Service
public class AuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public void registrarAuditoria(String usuario, String modulo, String accion,
                                   String descripcion, Object valorAnterior, Object valorNuevo) {
        try {
            SimpleFilterProvider filters = new SimpleFilterProvider()
                    .addFilter("productoFilter", SimpleBeanPropertyFilter.serializeAllExcept("imagen"));

            String nuevo = valorNuevo != null
                    ? objectMapper.writer(filters).writeValueAsString(valorNuevo)
                    : null;

            String anterior = valorAnterior != null
                    ? objectMapper.writer(filters).writeValueAsString(valorAnterior)
                    : null;

            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setModulo(modulo);
            auditoria.setAccion(accion);
            auditoria.setDescripcion(descripcion);
            auditoria.setFechaHora(LocalDateTime.now());
            auditoria.setValorAnterior(anterior);
            auditoria.setValorNuevo(nuevo);

            auditoriaRepository.save(auditoria);
        } catch (Exception e) {
            // En caso de error al serializar, guardar sin valores para evitar caída
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setModulo(modulo);
            auditoria.setAccion(accion);
            auditoria.setDescripcion("ERROR al serializar datos de auditoría: " + e.getMessage());
            auditoria.setFechaHora(LocalDateTime.now());
            auditoriaRepository.save(auditoria);
        }
    }
}
