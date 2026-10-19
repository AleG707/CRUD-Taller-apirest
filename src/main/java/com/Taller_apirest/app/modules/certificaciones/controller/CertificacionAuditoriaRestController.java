package com.Taller_apirest.app.modules.certificaciones.controller;

import com.Taller_apirest.app.modules.certificaciones.model.CertificacionAuditoria;
import com.Taller_apirest.app.modules.certificaciones.model.ItemChecklist;
import com.Taller_apirest.app.modules.certificaciones.service.CertificacionAuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/certificaciones")
@Tag(name = "Módulo Certificaciones: Auditoría de Calidad", description = "Operaciones para validación de normas de calidad, seguridad y emisión de certificados de trazabilidad")
public class CertificacionAuditoriaRestController {

    private final CertificacionAuditoriaService certificacionService;

    public CertificacionAuditoriaRestController(CertificacionAuditoriaService certificacionService) {
        this.certificacionService = certificacionService;
    }

    @GetMapping
    @Operation(summary = "Listar certificaciones", description = "Obtiene los certificados de calidad emitidos, con filtro opcional por placa de vehículo")
    public List<CertificacionAuditoria> listar(@RequestParam(required = false) String placa) {
        if (placa != null && !placa.isBlank()) {
            return certificacionService.listarPorPlaca(placa);
        }
        return certificacionService.listarTodas();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar certificación por ID", description = "Obtiene los detalles del certificado de auditoría")
    public ResponseEntity<CertificacionAuditoria> buscarPorId(@PathVariable String id) {
        return certificacionService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Consultar certificado por código único", description = "Permite validar la autenticidad y trazabilidad de un certificado emitido")
    public ResponseEntity<CertificacionAuditoria> buscarPorCodigo(@PathVariable String codigo) {
        return certificacionService.buscarPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/orden/{ordenId}")
    @Operation(summary = "Buscar certificación por orden de trabajo", description = "Obtiene el certificado correspondiente a una orden de trabajo específica")
    public ResponseEntity<CertificacionAuditoria> buscarPorOrdenId(@PathVariable String ordenId) {
        return certificacionService.buscarPorOrdenId(ordenId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/checklist-estandar")
    @Operation(summary = "Obtener checklist de auditoría estándar", description = "Retorna la lista de verificación basada en normas ISO y seguridad vehicular")
    public List<ItemChecklist> obtenerChecklistEstandar() {
        return certificacionService.generarChecklistEstandar();
    }

    @PostMapping
    @Operation(summary = "Auditar y emitir certificado de calidad (Auditor)", description = "El certificador/auditor valida la orden de trabajo, califica el checklist y emite el certificado digital con hash inmutable")
    public ResponseEntity<CertificacionAuditoria> auditar(@RequestBody CertificacionAuditoria certificacion) {
        return new ResponseEntity<>(certificacionService.auditarYCertificar(certificacion), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar certificación", description = "Elimina un certificado emitido")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        certificacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
