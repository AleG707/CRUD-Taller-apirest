package com.Taller_apirest.app.modules.admin.controller;

import com.Taller_apirest.app.modules.admin.model.RepuestoCatalogo;
import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/repuestos")
@Tag(name = "Módulo Admin: Catálogo de Repuestos", description = "Operaciones CRUD para la gestión del catálogo de repuestos y control de inventario")
public class RepuestoCatalogoRestController {

    private final RepuestoCatalogoService repuestoCatalogoService;

    public RepuestoCatalogoRestController(RepuestoCatalogoService repuestoCatalogoService) {
        this.repuestoCatalogoService = repuestoCatalogoService;
    }

    @GetMapping
    @Operation(summary = "Listar todos los repuestos", description = "Obtiene los repuestos disponibles en el inventario del taller")
    public List<RepuestoCatalogo> listarTodos(@RequestParam(required = false, defaultValue = "false") boolean soloBajoStock) {
        if (soloBajoStock) {
            return repuestoCatalogoService.listarBajoStock();
        }
        return repuestoCatalogoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar repuesto por ID", description = "Obtiene el detalle de un repuesto por su identificador")
    public ResponseEntity<RepuestoCatalogo> buscarPorId(@PathVariable String id) {
        return repuestoCatalogoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear nuevo repuesto", description = "Agrega un nuevo repuesto al catálogo del taller")
    public ResponseEntity<RepuestoCatalogo> crear(@RequestBody RepuestoCatalogo repuesto) {
        return new ResponseEntity<>(repuestoCatalogoService.guardar(repuesto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar repuesto", description = "Modifica los datos de un repuesto existente")
    public ResponseEntity<RepuestoCatalogo> actualizar(@PathVariable String id, @RequestBody RepuestoCatalogo repuesto) {
        try {
            return ResponseEntity.ok(repuestoCatalogoService.actualizar(id, repuesto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{codigo}/stock")
    @Operation(summary = "Ajustar stock de un repuesto", description = "Suma o resta existencias del stock actual de un repuesto")
    public ResponseEntity<RepuestoCatalogo> ajustarStock(@PathVariable String codigo, @RequestParam int cantidad) {
        RepuestoCatalogo actualizado = repuestoCatalogoService.actualizarStock(codigo, cantidad);
        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar repuesto", description = "Elimina un repuesto del catálogo")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        repuestoCatalogoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
