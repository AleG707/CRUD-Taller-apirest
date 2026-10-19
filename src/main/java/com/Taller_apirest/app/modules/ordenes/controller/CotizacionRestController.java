package com.Taller_apirest.app.modules.ordenes.controller;

import com.Taller_apirest.app.modules.ordenes.model.Cotizacion;
import com.Taller_apirest.app.modules.ordenes.service.CotizacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cotizaciones")
@Tag(name = "Módulo Órdenes: Cotizaciones", description = "Operaciones para creación, aprobación y rechazo de cotizaciones por clientes")
public class CotizacionRestController {

    private final CotizacionService cotizacionService;

    public CotizacionRestController(CotizacionService cotizacionService) {
        this.cotizacionService = cotizacionService;
    }

    @GetMapping
    @Operation(summary = "Listar cotizaciones", description = "Retorna todas las cotizaciones con opción de filtrar por cliente o placa")
    public List<Cotizacion> listar(@RequestParam(required = false) String clienteId,
                                   @RequestParam(required = false) String placa) {
        if (clienteId != null && !clienteId.isBlank()) {
            return cotizacionService.listarPorCliente(clienteId);
        }
        if (placa != null && !placa.isBlank()) {
            return cotizacionService.listarPorPlaca(placa);
        }
        return cotizacionService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cotización por ID", description = "Retorna los detalles de la cotización")
    public ResponseEntity<Cotizacion> buscarPorId(@PathVariable String id) {
        return cotizacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva cotización", description = "Genera una cotización estimada para los servicios de un vehículo")
    public ResponseEntity<Cotizacion> crear(@RequestBody Cotizacion cotizacion) {
        return new ResponseEntity<>(cotizacionService.crear(cotizacion), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/aprobar")
    @Operation(summary = "Aprobar cotización (Cliente)", description = "El cliente aprueba los servicios propuestos y se genera automáticamente la Orden de Trabajo")
    public ResponseEntity<Cotizacion> aprobar(@PathVariable String id, @RequestParam(required = false) String comentario) {
        try {
            return ResponseEntity.ok(cotizacionService.aprobar(id, comentario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar cotización (Cliente)", description = "El cliente rechaza la cotización con un motivo")
    public ResponseEntity<Cotizacion> rechazar(@PathVariable String id, @RequestParam(required = false) String motivo) {
        try {
            return ResponseEntity.ok(cotizacionService.rechazar(id, motivo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar cotización", description = "Elimina un registro de cotización")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        cotizacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
