package com.Taller_apirest.app.modules.admin.service;

import com.Taller_apirest.app.modules.admin.model.Rol;
import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.modules.admin.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarPorId(String id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public List<Usuario> listarPorRol(Rol rol) {
        return usuarioRepository.findByRol(rol);
    }

    public Usuario guardar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizar(String id, Usuario datosActualizados) {
        return usuarioRepository.findById(id).map(u -> {
            u.setNombre(datosActualizados.getNombre());
            u.setEmail(datosActualizados.getEmail());
            if (datosActualizados.getPassword() != null && !datosActualizados.getPassword().isBlank()) {
                u.setPassword(datosActualizados.getPassword());
            }
            u.setRol(datosActualizados.getRol());
            u.setTelefono(datosActualizados.getTelefono());
            u.setActivo(datosActualizados.isActivo());
            return usuarioRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        usuarioRepository.deleteById(id);
    }
}
