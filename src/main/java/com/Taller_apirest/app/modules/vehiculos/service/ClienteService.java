package com.Taller_apirest.app.modules.vehiculos.service;

import com.Taller_apirest.app.modules.vehiculos.model.Cliente;
import com.Taller_apirest.app.modules.vehiculos.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    public Optional<Cliente> buscarPorId(String id) {
        return clienteRepository.findById(id);
    }

    public Optional<Cliente> buscarPorIdentificacion(String identificacion) {
        return clienteRepository.findByIdentificacion(identificacion);
    }

    public Optional<Cliente> buscarPorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public Cliente guardar(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Cliente actualizar(String id, Cliente datos) {
        return clienteRepository.findById(id).map(c -> {
            c.setIdentificacion(datos.getIdentificacion());
            c.setNombreCompleto(datos.getNombreCompleto());
            c.setEmail(datos.getEmail());
            c.setTelefono(datos.getTelefono());
            c.setDireccion(datos.getDireccion());
            return clienteRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        clienteRepository.deleteById(id);
    }
}
