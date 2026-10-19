package com.Taller_apirest.app.modules.vehiculos.controller;

import com.Taller_apirest.app.modules.vehiculos.model.Vehiculo;
import com.Taller_apirest.app.modules.vehiculos.service.VehiculoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vehiculos")
@Tag(name = "Módulo Clientes: Gestión de Vehículos", description = "Operaciones CRUD para el registro y trazabilidad de vehículos")
public class VehiculoRestController {

    private final VehiculoService vehiculoService;

    public VehiculoRestController(VehiculoService vehiculoService) {
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los vehículos", description = "Retorna la flota completa de vehículos registrados")
    public List<Vehiculo> listarTodos(@RequestParam(required = false) String clienteId) {
        if (clienteId != null && !clienteId.isBlank()) {
            return vehiculoService.listarPorCliente(clienteId);
        }
        return vehiculoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar vehículo por ID", description = "Obtiene los datos del vehículo por su ID")
    public ResponseEntity<Vehiculo> buscarPorId(@PathVariable String id) {
        return vehiculoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/placa/{placa}")
    @Operation(summary = "Buscar vehículo por placa", description = "Obtiene los datos técnicos de un vehículo por su número de placa")
    public ResponseEntity<Vehiculo> buscarPorPlaca(@PathVariable String placa) {
        return vehiculoService.buscarPorPlaca(placa)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo vehículo", description = "Registra un vehículo asociado a un cliente")
    public ResponseEntity<Vehiculo> crear(@RequestBody Vehiculo vehiculo) {
        return new ResponseEntity<>(vehiculoService.guardar(vehiculo), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar vehículo", description = "Modifica los datos técnicos de un vehículo existente")
    public ResponseEntity<Vehiculo> actualizar(@PathVariable String id, @RequestBody Vehiculo vehiculo) {
        try {
            return ResponseEntity.ok(vehiculoService.actualizar(id, vehiculo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/kilometraje")
    @Operation(summary = "Actualizar kilometraje", description = "Actualiza la lectura de odómetro/kilometraje actual del vehículo")
    public ResponseEntity<Vehiculo> actualizarKilometraje(@PathVariable String id, @RequestParam Integer kilometraje) {
        try {
            return ResponseEntity.ok(vehiculoService.actualizarKilometraje(id, kilometraje));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar vehículo", description = "Elimina el registro de un vehículo por su ID")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        vehiculoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
