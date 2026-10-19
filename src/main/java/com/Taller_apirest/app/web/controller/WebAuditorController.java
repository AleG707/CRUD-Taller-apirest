package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.certificaciones.model.CertificacionAuditoria;
import com.Taller_apirest.app.modules.certificaciones.model.ItemChecklist;
import com.Taller_apirest.app.modules.certificaciones.service.CertificacionAuditoriaService;
import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/web/auditor")
public class WebAuditorController {

    private final CertificacionAuditoriaService certificacionService;
    private final OrdenTrabajoService ordenTrabajoService;

    public WebAuditorController(CertificacionAuditoriaService certificacionService,
                                OrdenTrabajoService ordenTrabajoService) {
        this.certificacionService = certificacionService;
        this.ordenTrabajoService = ordenTrabajoService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "certificados") String tab) {
        model.addAttribute("tabActiva", tab);
        model.addAttribute("certificados", certificacionService.listarTodas());
        model.addAttribute("ordenesParaAuditar", ordenTrabajoService.listarTodas());
        model.addAttribute("checklistEstandar", certificacionService.generarChecklistEstandar());
        return "auditor/index";
    }

    @PostMapping("/certificar")
    public String certificarOrden(@RequestParam String ordenTrabajoId,
                                  @RequestParam String auditorNombre,
                                  @RequestParam(required = false) String observaciones,
                                  @RequestParam(required = false) List<String> normasCumplidas,
                                  RedirectAttributes flash) {
        try {
            ordenTrabajoService.buscarPorId(ordenTrabajoId).ifPresent(orden -> {
                List<ItemChecklist> checklistEstandar = certificacionService.generarChecklistEstandar();
                List<ItemChecklist> checklistEvaluado = new ArrayList<>();

                for (ItemChecklist item : checklistEstandar) {
                    boolean cumplido = (normasCumplidas != null && normasCumplidas.contains(item.getNorma()));
                    checklistEvaluado.add(new ItemChecklist(item.getNorma(), item.getDescripcion(), cumplido,
                            cumplido ? "Conforme según auditoría" : "Observación técnica"));
                }

                CertificacionAuditoria cert = new CertificacionAuditoria();
                cert.setOrdenTrabajoId(orden.getId());
                cert.setNumeroOrden(orden.getNumeroOrden());
                cert.setVehiculoPlaca(orden.getVehiculoPlaca());
                cert.setMecanicoNombre(orden.getMecanicoNombre());
                cert.setAuditorId("aud-web");
                cert.setAuditorNombre(auditorNombre);
                cert.setObservacionesAuditor(observaciones);
                cert.setChecklist(checklistEvaluado);

                CertificacionAuditoria emitido = certificacionService.auditarYCertificar(cert);
                flash.addFlashAttribute("mensajeExito", "Certificado " + emitido.getCodigoCertificado() + " emitido con éxito. Hash: " + emitido.getHashTrazabilidad().substring(0, 16) + "...");
            });
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al emitir certificación: " + e.getMessage());
        }
        return "redirect:/web/auditor?tab=certificados";
    }

    @GetMapping("/certificado/{id}")
    public String verCertificadoDetalle(@PathVariable String id, Model model) {
        certificacionService.buscarPorId(id).ifPresent(c -> {
            model.addAttribute("cert", c);
            if (c.getOrdenTrabajoId() != null) {
                ordenTrabajoService.buscarPorId(c.getOrdenTrabajoId()).ifPresent(o -> model.addAttribute("orden", o));
            }
        });
        return "auditor/certificado-detalle";
    }
}
