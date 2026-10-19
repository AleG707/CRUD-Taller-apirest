package com.Taller_apirest.app.modules.admin.controller;

import com.Taller_apirest.app.modules.admin.model.ServicioCatalogo;
import com.Taller_apirest.app.modules.admin.service.ServicioCatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/servicios")
@Tag(name = "Módulo Admin: Catálogo de Servicios", description = "Operaciones CRUD para la gestión del catálogo maestro de servicios del taller")
public class ServicioCatalogoRestController {

    private final ServicioCatalogoService servicioCatalogoService;

    public ServicioCatalogoRestController(ServicioCatalogoService servicioCatalogoService) {
        this.servicioCatalogoService = servicioCatalogoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los servicios del catálogo", description = "Obtiene los servicios disponibles en el taller")
    public List<ServicioCatalogo> listarTodos(@RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        if (soloActivos) {
            return servicioCatalogoService.listarActivos();
        }
        return servicioCatalogoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar servicio por ID", description = "Obtiene el detalle de un servicio por su identificador")
    public ResponseEntity<ServicioCatalogo> buscarPorId(@PathVariable String id) {
        return servicioCatalogoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo servicio", description = "Agrega un nuevo servicio al catálogo")
    public ResponseEntity<ServicioCatalogo> crear(@RequestBody ServicioCatalogo servicio) {
        return new ResponseEntity<>(servicioCatalogoService.guardar(servicio), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar servicio", description = "Modifica los datos de un servicio existente")
    public ResponseEntity<ServicioCatalogo> actualizar(@PathVariable String id, @RequestBody ServicioCatalogo servicio) {
        try {
            return ResponseEntity.ok(servicioCatalogoService.actualizar(id, servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar servicio", description = "Elimina un servicio del catálogo")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        servicioCatalogoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
