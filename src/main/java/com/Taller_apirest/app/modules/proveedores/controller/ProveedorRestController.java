package com.Taller_apirest.app.modules.proveedores.controller;

import com.Taller_apirest.app.modules.proveedores.model.Proveedor;
import com.Taller_apirest.app.modules.proveedores.model.SuministroProveedor;
import com.Taller_apirest.app.modules.proveedores.service.ProveedorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/proveedores")
@Tag(name = "Módulo Proveedores: Proveedores y Suministros", description = "Operaciones para la gestión de proveedores e inventario mayorista de repuestos")
public class ProveedorRestController {

    private final ProveedorService proveedorService;

    public ProveedorRestController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    @Operation(summary = "Listar proveedores", description = "Obtiene la lista de proveedores asociados al taller")
    public List<Proveedor> listar(@RequestParam(required = false, defaultValue = "false") boolean soloActivos) {
        if (soloActivos) {
            return proveedorService.listarActivos();
        }
        return proveedorService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar proveedor por ID", description = "Obtiene los detalles del proveedor")
    public ResponseEntity<Proveedor> buscarPorId(@PathVariable String id) {
        return proveedorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Registrar nuevo proveedor", description = "Agrega un proveedor a la base de datos de proveedores")
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        return new ResponseEntity<>(proveedorService.guardar(proveedor), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar datos del proveedor", description = "Modifica la información comercial del proveedor")
    public ResponseEntity<Proveedor> actualizar(@PathVariable String id, @RequestBody Proveedor proveedor) {
        try {
            return ResponseEntity.ok(proveedorService.actualizar(id, proveedor));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/suministros")
    @Operation(summary = "Agregar suministro al catálogo del proveedor", description = "Añade un repuesto o insumo al catálogo mayorista del proveedor")
    public ResponseEntity<Proveedor> agregarSuministro(@PathVariable String id, @RequestBody SuministroProveedor suministro) {
        try {
            return ResponseEntity.ok(proveedorService.agregarSuministro(id, suministro));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor del sistema")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        proveedorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
