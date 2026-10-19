package com.Taller_apirest.app.modules.ordenes.service;

import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.model.OrdenTrabajo;
import com.Taller_apirest.app.modules.ordenes.model.RegistroTiempo;
import com.Taller_apirest.app.modules.ordenes.model.RepuestoUtilizado;
import com.Taller_apirest.app.modules.ordenes.repository.OrdenTrabajoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OrdenTrabajoService {

    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final RepuestoCatalogoService repuestoCatalogoService;
    private static final double TARIFA_HORA_DEFAULT = 45000.0; // Pesos por hora de taller

    public OrdenTrabajoService(OrdenTrabajoRepository ordenTrabajoRepository,
                               RepuestoCatalogoService repuestoCatalogoService) {
        this.ordenTrabajoRepository = ordenTrabajoRepository;
        this.repuestoCatalogoService = repuestoCatalogoService;
    }

    public List<OrdenTrabajo> listarTodas() {
        return ordenTrabajoRepository.findAll();
    }

    public Optional<OrdenTrabajo> buscarPorId(String id) {
        return ordenTrabajoRepository.findById(id);
    }

    public Optional<OrdenTrabajo> buscarPorNumeroOrden(String numeroOrden) {
        return ordenTrabajoRepository.findByNumeroOrden(numeroOrden);
    }

    public List<OrdenTrabajo> listarPorMecanico(String mecanicoId) {
        return ordenTrabajoRepository.findByMecanicoId(mecanicoId);
    }

    public List<OrdenTrabajo> listarPorPlaca(String placa) {
        return ordenTrabajoRepository.findByVehiculoPlaca(placa.toUpperCase().trim());
    }

    public List<OrdenTrabajo> listarPorCliente(String clienteId) {
        return ordenTrabajoRepository.findByClienteId(clienteId);
    }

    public List<OrdenTrabajo> listarPorEstado(EstadoOrden estado) {
        return ordenTrabajoRepository.findByEstado(estado);
    }

    public OrdenTrabajo crear(OrdenTrabajo orden) {
        if (orden.getNumeroOrden() == null || orden.getNumeroOrden().isBlank()) {
            orden.setNumeroOrden("OT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        if (orden.getEstado() == null) {
            orden.setEstado(EstadoOrden.ASIGNADA);
        }
        if (orden.getFechaIngreso() == null) {
            orden.setFechaIngreso(LocalDateTime.now());
        }
        orden.recalcularCostos(TARIFA_HORA_DEFAULT);
        return ordenTrabajoRepository.save(orden);
    }

    public OrdenTrabajo actualizar(String id, OrdenTrabajo datos) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.setMotivoIngreso(datos.getMotivoIngreso());
            o.setKilometrajeIngreso(datos.getKilometrajeIngreso());
            o.setMecanicoId(datos.getMecanicoId());
            o.setMecanicoNombre(datos.getMecanicoNombre());
            o.setEstado(datos.getEstado());
            o.setDiagnosticoTecnico(datos.getDiagnosticoTecnico());
            o.setReparacionesEfectuadas(datos.getReparacionesEfectuadas());
            o.recalcularCostos(TARIFA_HORA_DEFAULT);
            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden de trabajo no encontrada con ID: " + id));
    }

    public OrdenTrabajo registrarDiagnostico(String id, String diagnostico) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.setDiagnosticoTecnico(diagnostico);
            if (o.getEstado() == EstadoOrden.ASIGNADA) {
                o.setEstado(EstadoOrden.EN_DIAGNOSTICO);
            }
            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    public OrdenTrabajo registrarReparaciones(String id, String reparaciones) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.setReparacionesEfectuadas(reparaciones);
            o.setEstado(EstadoOrden.EN_REPARACION);
            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    public OrdenTrabajo agregarRepuestoUtilizado(String id, RepuestoUtilizado repuesto) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.getRepuestosUtilizados().add(repuesto);
            o.recalcularCostos(TARIFA_HORA_DEFAULT);

            // Descontar del inventario del taller
            if (repuesto.getCodigo() != null && repuesto.getCantidad() != null) {
                repuestoCatalogoService.actualizarStock(repuesto.getCodigo(), -repuesto.getCantidad());
            }

            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    public OrdenTrabajo agregarRegistroTiempo(String id, RegistroTiempo registro) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.getRegistrosTiempo().add(registro);
            o.recalcularCostos(TARIFA_HORA_DEFAULT);
            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    public OrdenTrabajo cambiarEstado(String id, EstadoOrden nuevoEstado) {
        return ordenTrabajoRepository.findById(id).map(o -> {
            o.setEstado(nuevoEstado);
            if (nuevoEstado == EstadoOrden.COMPLETADA && o.getFechaFinalizacion() == null) {
                o.setFechaFinalizacion(LocalDateTime.now());
            }
            return ordenTrabajoRepository.save(o);
        }).orElseThrow(() -> new RuntimeException("Orden no encontrada con ID: " + id));
    }

    public void eliminar(String id) {
        ordenTrabajoRepository.deleteById(id);
    }
}
