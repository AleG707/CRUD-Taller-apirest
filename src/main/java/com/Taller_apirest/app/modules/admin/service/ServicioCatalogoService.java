package com.Taller_apirest.app.modules.admin.service;

import com.Taller_apirest.app.modules.admin.model.ServicioCatalogo;
import com.Taller_apirest.app.modules.admin.repository.ServicioCatalogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicioCatalogoService {

    private final ServicioCatalogoRepository servicioCatalogoRepository;

    public ServicioCatalogoService(ServicioCatalogoRepository servicioCatalogoRepository) {
        this.servicioCatalogoRepository = servicioCatalogoRepository;
    }

    public List<ServicioCatalogo> listarTodos() {
        return servicioCatalogoRepository.findAll();
    }

    public List<ServicioCatalogo> listarActivos() {
        return servicioCatalogoRepository.findByActivoTrue();
    }

    public Optional<ServicioCatalogo> buscarPorId(String id) {
        return servicioCatalogoRepository.findById(id);
    }

    public Optional<ServicioCatalogo> buscarPorCodigo(String codigo) {
        return servicioCatalogoRepository.findByCodigo(codigo);
    }

    public ServicioCatalogo guardar(ServicioCatalogo servicio) {
        return servicioCatalogoRepository.save(servicio);
    }

    public ServicioCatalogo actualizar(String id, ServicioCatalogo datos) {
        return servicioCatalogoRepository.findById(id).map(s -> {
            s.setCodigo(datos.getCodigo());
            s.setNombre(datos.getNombre());
            s.setCategoria(datos.getCategoria());
            s.setPrecioBase(datos.getPrecioBase());
            s.setDuracionMinutos(datos.getDuracionMinutos());
            s.setDescripcion(datos.getDescripcion());
            s.setActivo(datos.isActivo());
            return servicioCatalogoRepository.save(s);
        }).orElseThrow(() -> new RuntimeException("Servicio no encontrado con ID: " + id));
    }

    public void eliminar(String id) {
        servicioCatalogoRepository.deleteById(id);
    }
}
