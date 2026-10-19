package com.Taller_apirest.app.modules.ordenes.service;

import com.Taller_apirest.app.modules.ordenes.model.Cotizacion;
import com.Taller_apirest.app.modules.ordenes.model.EstadoCotizacion;
import com.Taller_apirest.app.modules.ordenes.model.OrdenTrabajo;
import com.Taller_apirest.app.modules.ordenes.repository.CotizacionRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CotizacionService {

    private final CotizacionRepository cotizacionRepository;
    private final OrdenTrabajoService ordenTrabajoService;

    public CotizacionService(CotizacionRepository cotizacionRepository, @Lazy OrdenTrabajoService ordenTrabajoService) {
        this.cotizacionRepository = cotizacionRepository;
        this.ordenTrabajoService = ordenTrabajoService;
    }

    public List<Cotizacion> listarTodas() {
        return cotizacionRepository.findAll();
    }

    public Optional<Cotizacion> buscarPorId(String id) {
        return cotizacionRepository.findById(id);
    }

    public List<Cotizacion> listarPorCliente(String clienteId) {
        return cotizacionRepository.findByClienteId(clienteId);
    }

    public List<Cotizacion> listarPorPlaca(String placa) {
        return cotizacionRepository.findByPlaca(placa);
    }

    public Cotizacion crear(Cotizacion cotizacion) {
        if (cotizacion.getCodigoCotizacion() == null || cotizacion.getCodigoCotizacion().isBlank()) {
            cotizacion.setCodigoCotizacion("COT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        }
        cotizacion.setEstado(EstadoCotizacion.PENDIENTE);
        cotizacion.setFechaCreacion(LocalDateTime.now());
        return cotizacionRepository.save(cotizacion);
    }

    public Cotizacion aprobar(String id, String respuestaCliente) {
        return cotizacionRepository.findById(id).map(c -> {
            c.setEstado(EstadoCotizacion.APROBADA);
            c.setRespuestaCliente(respuestaCliente != null ? respuestaCliente : "Aprobado por el cliente.");
            c.setFechaRespuesta(LocalDateTime.now());
            Cotizacion guardada = cotizacionRepository.save(c);

            // Generar orden de trabajo automáticamente al ser aprobada
            OrdenTrabajo orden = new OrdenTrabajo();
            orden.setCotizacionId(guardada.getId());
            orden.setClienteId(guardada.getClienteId());
            orden.setClienteNombre(guardada.getClienteNombre());
            orden.setVehiculoId(guardada.getVehiculoId());
            orden.setVehiculoPlaca(guardada.getPlaca());
            orden.setMotivoIngreso("Servicios aprobados: " + String.join(", ", guardada.getServiciosSolicitados()));
            ordenTrabajoService.crear(orden);

            return guardada;
        }).orElseThrow(() -> new RuntimeException("Cotización no encontrada con ID: " + id));
    }

    public Cotizacion rechazar(String id, String motivoRechazo) {
        return cotizacionRepository.findById(id).map(c -> {
            c.setEstado(EstadoCotizacion.RECHAZADA);
            c.setRespuestaCliente(motivoRechazo != null ? motivoRechazo : "Rechazado por el cliente.");
            c.setFechaRespuesta(LocalDateTime.now());
            return cotizacionRepository.save(c);
        }).orElseThrow(() -> new RuntimeException("Cotización no encontrada con ID: " + id));
    }

    public void eliminar(String id) {
        cotizacionRepository.deleteById(id);
    }
}
