package com.Taller_apirest.app.modules.ordenes.controller;

import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.model.OrdenTrabajo;
import com.Taller_apirest.app.modules.ordenes.model.RegistroTiempo;
import com.Taller_apirest.app.modules.ordenes.model.RepuestoUtilizado;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ordenes")
@Tag(name = "Módulo Órdenes: Órdenes de Trabajo y Trazabilidad", description = "Operaciones para gestión de órdenes de trabajo, diagnósticos, repuestos y horas de servicio del mecánico")
public class OrdenTrabajoRestController {

    private final OrdenTrabajoService ordenTrabajoService;

    public OrdenTrabajoRestController(OrdenTrabajoService ordenTrabajoService) {
        this.ordenTrabajoService = ordenTrabajoService;
    }

    @GetMapping
    @Operation(summary = "Listar órdenes de trabajo", description = "Obtiene las órdenes de trabajo con filtros por mecánico, placa, cliente o estado")
    public List<OrdenTrabajo> listar(@RequestParam(required = false) String mecanicoId,
                                     @RequestParam(required = false) String placa,
                                     @RequestParam(required = false) String clienteId,
                                     @RequestParam(required = false) EstadoOrden estado) {
        if (mecanicoId != null && !mecanicoId.isBlank()) {
            return ordenTrabajoService.listarPorMecanico(mecanicoId);
        }
        if (placa != null && !placa.isBlank()) {
            return ordenTrabajoService.listarPorPlaca(placa);
        }
        if (clienteId != null && !clienteId.isBlank()) {
            return ordenTrabajoService.listarPorCliente(clienteId);
        }
        if (estado != null) {
            return ordenTrabajoService.listarPorEstado(estado);
        }
        return ordenTrabajoService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar orden por ID", description = "Obtiene todos los datos y la trazabilidad de una orden de trabajo")
    public ResponseEntity<OrdenTrabajo> buscarPorId(@PathVariable String id) {
        return ordenTrabajoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nueva orden de trabajo", description = "Registra una nueva orden de trabajo en el taller")
    public ResponseEntity<OrdenTrabajo> crear(@RequestBody OrdenTrabajo orden) {
        return new ResponseEntity<>(ordenTrabajoService.crear(orden), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar orden de trabajo", description = "Modifica los datos principales de una orden")
    public ResponseEntity<OrdenTrabajo> actualizar(@PathVariable String id, @RequestBody OrdenTrabajo orden) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.actualizar(id, orden));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/diagnostico")
    @Operation(summary = "Registrar diagnóstico (Mecánico)", description = "El mecánico registra el diagnóstico técnico de la orden")
    public ResponseEntity<OrdenTrabajo> registrarDiagnostico(@PathVariable String id, @RequestParam String diagnostico) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.registrarDiagnostico(id, diagnostico));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/reparaciones")
    @Operation(summary = "Registrar reparaciones (Mecánico)", description = "El mecánico registra las reparaciones y labores ejecutadas")
    public ResponseEntity<OrdenTrabajo> registrarReparaciones(@PathVariable String id, @RequestParam String reparaciones) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.registrarReparaciones(id, reparaciones));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/repuestos")
    @Operation(summary = "Agregar repuesto utilizado (Mecánico)", description = "Registra un repuesto utilizado en la reparación y descuenta del stock")
    public ResponseEntity<OrdenTrabajo> agregarRepuesto(@PathVariable String id, @RequestBody RepuestoUtilizado repuesto) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.agregarRepuestoUtilizado(id, repuesto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/tiempos")
    @Operation(summary = "Registrar tiempos de servicio (Mecánico)", description = "Registra horas y minutos invertidos en la reparación")
    public ResponseEntity<OrdenTrabajo> agregarTiempo(@PathVariable String id, @RequestBody RegistroTiempo registroTiempo) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.agregarRegistroTiempo(id, registroTiempo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/estado")
    @Operation(summary = "Cambiar estado de la orden", description = "Actualiza el estado del flujo de trabajo (ASIGNADA, EN_DIAGNOSTICO, EN_REPARACION, COMPLETADA, etc.)")
    public ResponseEntity<OrdenTrabajo> cambiarEstado(@PathVariable String id, @RequestParam EstadoOrden estado) {
        try {
            return ResponseEntity.ok(ordenTrabajoService.cambiarEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar orden de trabajo", description = "Elimina una orden de trabajo por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        ordenTrabajoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
