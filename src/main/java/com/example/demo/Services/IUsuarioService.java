package com.example.demo.Services;

import com.example.demo.Models.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    List<Usuario> listarTodos();

    Optional<Usuario> obtenerPorId(Long id);

    Optional<Usuario> obtenerPorCorreo(String correo);

    Usuario guardar(Usuario usuario);

    void eliminar(Long id);
}
