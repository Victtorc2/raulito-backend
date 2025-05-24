package com.example.demo.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class VentaDiariaDTO {
    private LocalDate fecha;
    private double total;
}
