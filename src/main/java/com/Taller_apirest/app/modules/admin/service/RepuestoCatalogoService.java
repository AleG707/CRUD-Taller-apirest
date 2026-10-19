package com.Taller_apirest.app.modules.admin.service;

import com.Taller_apirest.app.modules.admin.model.RepuestoCatalogo;
import com.Taller_apirest.app.modules.admin.repository.RepuestoCatalogoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RepuestoCatalogoService {

    private final RepuestoCatalogoRepository repuestoCatalogoRepository;

    public RepuestoCatalogoService(RepuestoCatalogoRepository repuestoCatalogoRepository) {
        this.repuestoCatalogoRepository = repuestoCatalogoRepository;
    }

    public List<RepuestoCatalogo> listarTodos() {
        return repuestoCatalogoRepository.findAll();
    }

    public Optional<RepuestoCatalogo> buscarPorId(String id) {
        return repuestoCatalogoRepository.findById(id);
    }

    public Optional<RepuestoCatalogo> buscarPorCodigo(String codigo) {
        return repuestoCatalogoRepository.findByCodigo(codigo);
    }

    public List<RepuestoCatalogo> listarBajoStock() {
        return repuestoCatalogoRepository.findAll().stream()
                .filter(r -> r.getStockActual() != null && r.getStockMinimo() != null && r.getStockActual() <= r.getStockMinimo())
                .toList();
    }

    public RepuestoCatalogo guardar(RepuestoCatalogo repuesto) {
        return repuestoCatalogoRepository.save(repuesto);
    }

    public RepuestoCatalogo actualizar(String id, RepuestoCatalogo datos) {
        return repuestoCatalogoRepository.findById(id).map(r -> {
            r.setCodigo(datos.getCodigo());
            r.setNombre(datos.getNombre());
            r.setCategoria(datos.getCategoria());
            r.setMarca(datos.getMarca());
            r.setPrecioVenta(datos.getPrecioVenta());
            r.setStockMinimo(datos.getStockMinimo());
            r.setStockActual(datos.getStockActual());
            r.setProveedorDefault(datos.getProveedorDefault());
            return repuestoCatalogoRepository.save(r);
        }).orElseThrow(() -> new RuntimeException("Repuesto no encontrado con ID: " + id));
    }

    public RepuestoCatalogo actualizarStock(String codigo, int cantidadCambio) {
        return repuestoCatalogoRepository.findByCodigo(codigo).map(r -> {
            int nuevoStock = (r.getStockActual() == null ? 0 : r.getStockActual()) + cantidadCambio;
            r.setStockActual(Math.max(0, nuevoStock));
            return repuestoCatalogoRepository.save(r);
        }).orElse(null);
    }

    public void eliminar(String id) {
        repuestoCatalogoRepository.deleteById(id);
    }
}
