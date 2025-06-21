package com.example.demo.Models;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario; // Usuario que realiza la acción
    private String modulo; // Módulo donde se realizó la acción (productos, ventas, movimientos)
    private String accion; // Tipo de acción (crear, actualizar, eliminar, etc.)
    private String descripcion; // Descripción de la acción realizada
    private LocalDateTime fechaHora; // Fecha y hora de la acción
    
    @Column(columnDefinition = "TEXT")
    private String valorAnterior; // Valor anterior antes de la acción

    @Column(columnDefinition = "TEXT")
    private String valorNuevo; // Valor nuevo después de la acción


}
