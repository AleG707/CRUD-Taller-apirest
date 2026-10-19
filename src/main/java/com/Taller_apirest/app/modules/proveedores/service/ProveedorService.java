package com.Taller_apirest.app.modules.proveedores.service;

import com.Taller_apirest.app.modules.proveedores.model.Proveedor;
import com.Taller_apirest.app.modules.proveedores.model.SuministroProveedor;
import com.Taller_apirest.app.modules.proveedores.repository.ProveedorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listarTodos() {
        return proveedorRepository.findAll();
    }

    public List<Proveedor> listarActivos() {
        return proveedorRepository.findByActivoTrue();
    }

    public Optional<Proveedor> buscarPorId(String id) {
        return proveedorRepository.findById(id);
    }

    public Optional<Proveedor> buscarPorNit(String nit) {
        return proveedorRepository.findByNit(nit);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public Proveedor actualizar(String id, Proveedor datos) {
        return proveedorRepository.findById(id).map(p -> {
            p.setNit(datos.getNit());
            p.setRazonSocial(datos.getRazonSocial());
            p.setContacto(datos.getContacto());
            p.setTelefono(datos.getTelefono());
            p.setEmail(datos.getEmail());
            p.setCiudad(datos.getCiudad());
            p.setActivo(datos.isActivo());
            if (datos.getSuministros() != null) {
                p.setSuministros(datos.getSuministros());
            }
            return proveedorRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    public Proveedor agregarSuministro(String id, SuministroProveedor suministro) {
        return proveedorRepository.findById(id).map(p -> {
            p.getSuministros().add(suministro);
            return proveedorRepository.save(p);
        }).orElseThrow(() -> new RuntimeException("Proveedor no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        proveedorRepository.deleteById(id);
    }
}
