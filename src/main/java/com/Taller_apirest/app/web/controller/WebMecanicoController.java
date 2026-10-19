package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import com.Taller_apirest.app.modules.ordenes.model.EstadoOrden;
import com.Taller_apirest.app.modules.ordenes.model.RegistroTiempo;
import com.Taller_apirest.app.modules.ordenes.model.RepuestoUtilizado;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/mecanico")
public class WebMecanicoController {

    private final OrdenTrabajoService ordenTrabajoService;
    private final RepuestoCatalogoService repuestoService;

    public WebMecanicoController(OrdenTrabajoService ordenTrabajoService,
                                 RepuestoCatalogoService repuestoService) {
        this.ordenTrabajoService = ordenTrabajoService;
        this.repuestoService = repuestoService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(required = false) EstadoOrden estado) {
        if (estado != null) {
            model.addAttribute("ordenes", ordenTrabajoService.listarPorEstado(estado));
        } else {
            model.addAttribute("ordenes", ordenTrabajoService.listarTodas());
        }
        model.addAttribute("estadoFiltro", estado);
        model.addAttribute("estados", EstadoOrden.values());
        model.addAttribute("catalogoRepuestos", repuestoService.listarTodos());
        return "mecanico/index";
    }

    @PostMapping("/ordenes/{id}/diagnostico")
    public String registrarDiagnostico(@PathVariable String id,
                                       @RequestParam String diagnosticoTecnico,
                                       RedirectAttributes flash) {
        try {
            ordenTrabajoService.registrarDiagnostico(id, diagnosticoTecnico);
            flash.addFlashAttribute("mensajeExito", "Diagnóstico técnico registrado con éxito.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar diagnóstico.");
        }
        return "redirect:/web/mecanico";
    }

    @PostMapping("/ordenes/{id}/reparaciones")
    public String registrarReparaciones(@PathVariable String id,
                                        @RequestParam String reparacionesEfectuadas,
                                        RedirectAttributes flash) {
        try {
            ordenTrabajoService.registrarReparaciones(id, reparacionesEfectuadas);
            flash.addFlashAttribute("mensajeExito", "Reparaciones registradas correctamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar reparaciones.");
        }
        return "redirect:/web/mecanico";
    }

    @PostMapping("/ordenes/{id}/repuestos")
    public String agregarRepuesto(@PathVariable String id,
                                  @RequestParam String repuestoCodigo,
                                  @RequestParam Integer cantidad,
                                  RedirectAttributes flash) {
        try {
            repuestoService.buscarPorCodigo(repuestoCodigo).ifPresent(r -> {
                RepuestoUtilizado utilizado = new RepuestoUtilizado(
                        r.getId(),
                        r.getCodigo(),
                        r.getNombre(),
                        cantidad,
                        r.getPrecioVenta()
                );
                ordenTrabajoService.agregarRepuestoUtilizado(id, utilizado);
            });
            flash.addFlashAttribute("mensajeExito", "Repuesto añadido y descontado del inventario.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al añadir repuesto a la orden.");
        }
        return "redirect:/web/mecanico";
    }

    @PostMapping("/ordenes/{id}/tiempos")
    public String agregarTiempo(@PathVariable String id,
                                @RequestParam String mecanicoNombre,
                                @RequestParam Integer horas,
                                @RequestParam Integer minutos,
                                @RequestParam String descripcionTarea,
                                RedirectAttributes flash) {
        try {
            RegistroTiempo registro = new RegistroTiempo(mecanicoNombre, horas, minutos, descripcionTarea);
            ordenTrabajoService.agregarRegistroTiempo(id, registro);
            flash.addFlashAttribute("mensajeExito", "Tiempo de servicio registrado.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar tiempo.");
        }
        return "redirect:/web/mecanico";
    }

    @PostMapping("/ordenes/{id}/estado")
    public String cambiarEstado(@PathVariable String id,
                                @RequestParam EstadoOrden estado,
                                RedirectAttributes flash) {
        try {
            ordenTrabajoService.cambiarEstado(id, estado);
            flash.addFlashAttribute("mensajeExito", "Estado de orden actualizado a " + estado);
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al cambiar estado de orden.");
        }
        return "redirect:/web/mecanico";
    }
}
