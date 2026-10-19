package com.Taller_apirest.app.modules.proveedores.controller;

import com.Taller_apirest.app.modules.proveedores.model.EstadoPedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.PedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.service.PedidoReabastecimientoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos-reabastecimiento")
@Tag(name = "Módulo Proveedores: Reabastecimiento", description = "Operaciones para órdenes de compra, despacho y entrega de repuestos por proveedores")
public class PedidoReabastecimientoRestController {

    private final PedidoReabastecimientoService pedidoService;

    public PedidoReabastecimientoRestController(PedidoReabastecimientoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    @Operation(summary = "Listar pedidos de reabastecimiento", description = "Obtiene los pedidos con opción de filtrar por proveedor o estado")
    public List<PedidoReabastecimiento> listar(@RequestParam(required = false) String proveedorId,
                                               @RequestParam(required = false) EstadoPedidoReabastecimiento estado) {
        if (proveedorId != null && !proveedorId.isBlank()) {
            return pedidoService.listarPorProveedor(proveedorId);
        }
        if (estado != null) {
            return pedidoService.listarPorEstado(estado);
        }
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Obtiene los detalles del pedido de reabastecimiento")
    public ResponseEntity<PedidoReabastecimiento> buscarPorId(@PathVariable String id) {
        return pedidoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Crear solicitud de reabastecimiento", description = "Genera una nueva orden de pedido hacia un proveedor")
    public ResponseEntity<PedidoReabastecimiento> crear(@RequestBody PedidoReabastecimiento pedido) {
        return new ResponseEntity<>(pedidoService.crear(pedido), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/despachar")
    @Operation(summary = "Despachar pedido (Proveedor)", description = "El proveedor registra que el pedido fue despachado y suministra número de guía y transportadora")
    public ResponseEntity<PedidoReabastecimiento> despachar(@PathVariable String id,
                                                            @RequestParam String transportadora,
                                                            @RequestParam String numeroGuia) {
        try {
            return ResponseEntity.ok(pedidoService.despachar(id, transportadora, numeroGuia));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/entregar")
    @Operation(summary = "Registrar entrega recibida (Taller / Proveedor)", description = "Confirma la entrega del repuesto y reabastece el inventario automáticamente")
    public ResponseEntity<PedidoReabastecimiento> entregar(@PathVariable String id,
                                                           @RequestParam(required = false) String observaciones) {
        try {
            return ResponseEntity.ok(pedidoService.registrarEntrega(id, observaciones));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido de reabastecimiento")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        pedidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
