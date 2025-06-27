package com.example.demo.Services.Impl;

import com.example.demo.Models.Auditoria;
import com.example.demo.Repositories.AuditoriaRepository;
import com.example.demo.Services.IAuditoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AuditoriaServiceImpl implements IAuditoriaService {

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
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
            Auditoria auditoria = new Auditoria();
            auditoria.setUsuario(usuario);
            auditoria.setModulo(modulo);
            auditoria.setAccion(accion);
            auditoria.setDescripcion("ERROR al serializar datos de auditoría: " + e.getMessage());
            auditoria.setFechaHora(LocalDateTime.now());
            auditoriaRepository.save(auditoria);
        }
    }

    @Override
    public List<Auditoria> obtenerTodos() {
        return auditoriaRepository.findAll();
    }

    @Override
    public Optional<Auditoria> obtenerPorId(Long id) {
        return auditoriaRepository.findById(id);
    }
}