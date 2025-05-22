package com.example.demo.DTO;

import lombok.Data;

@Data
public class AuthRequest {
    private String correo;
    private String password;
}
