package com.Taller_apirest.app.modules.vehiculos.service;

import com.Taller_apirest.app.modules.vehiculos.model.Vehiculo;
import com.Taller_apirest.app.modules.vehiculos.repository.VehiculoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    public VehiculoService(VehiculoRepository vehiculoRepository) {
        this.vehiculoRepository = vehiculoRepository;
    }

    public List<Vehiculo> listarTodos() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> buscarPorId(String id) {
        return vehiculoRepository.findById(id);
    }

    public Optional<Vehiculo> buscarPorPlaca(String placa) {
        return vehiculoRepository.findByPlaca(placa.toUpperCase().trim());
    }

    public List<Vehiculo> listarPorCliente(String clienteId) {
        return vehiculoRepository.findByClienteId(clienteId);
    }

    public Vehiculo guardar(Vehiculo vehiculo) {
        if (vehiculo.getPlaca() != null) {
            vehiculo.setPlaca(vehiculo.getPlaca().toUpperCase().trim());
        }
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo actualizar(String id, Vehiculo datos) {
        return vehiculoRepository.findById(id).map(v -> {
            if (datos.getPlaca() != null) {
                v.setPlaca(datos.getPlaca().toUpperCase().trim());
            }
            v.setMarca(datos.getMarca());
            v.setModelo(datos.getModelo());
            v.setAnio(datos.getAnio());
            v.setKilometraje(datos.getKilometraje());
            v.setVin(datos.getVin());
            v.setColor(datos.getColor());
            if (datos.getClienteId() != null) {
                v.setClienteId(datos.getClienteId());
            }
            if (datos.getClienteNombre() != null) {
                v.setClienteNombre(datos.getClienteNombre());
            }
            return vehiculoRepository.save(v);
        }).orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    public Vehiculo actualizarKilometraje(String id, Integer nuevoKm) {
        return vehiculoRepository.findById(id).map(v -> {
            v.setKilometraje(nuevoKm);
            return vehiculoRepository.save(v);
        }).orElseThrow(() -> new RuntimeException("Vehículo no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        vehiculoRepository.deleteById(id);
    }
}
