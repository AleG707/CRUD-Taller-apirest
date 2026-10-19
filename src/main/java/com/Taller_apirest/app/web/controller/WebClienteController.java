package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.certificaciones.service.CertificacionAuditoriaService;
import com.Taller_apirest.app.modules.ordenes.service.CotizacionService;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import com.Taller_apirest.app.modules.vehiculos.model.Cliente;
import com.Taller_apirest.app.modules.vehiculos.model.Vehiculo;
import com.Taller_apirest.app.modules.vehiculos.service.ClienteService;
import com.Taller_apirest.app.modules.vehiculos.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/cliente")
public class WebClienteController {

    private final VehiculoService vehiculoService;
    private final ClienteService clienteService;
    private final CotizacionService cotizacionService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final CertificacionAuditoriaService certificacionService;

    public WebClienteController(VehiculoService vehiculoService,
                                ClienteService clienteService,
                                CotizacionService cotizacionService,
                                OrdenTrabajoService ordenTrabajoService,
                                CertificacionAuditoriaService certificacionService) {
        this.vehiculoService = vehiculoService;
        this.clienteService = clienteService;
        this.cotizacionService = cotizacionService;
        this.ordenTrabajoService = ordenTrabajoService;
        this.certificacionService = certificacionService;
    }

    @GetMapping
    public String index(Model model,
                        @RequestParam(defaultValue = "vehiculos") String tab,
                        @RequestParam(required = false) String placaBuscar) {
        model.addAttribute("tabActiva", tab);
        model.addAttribute("vehiculos", vehiculoService.listarTodos());
        model.addAttribute("clientes", clienteService.listarTodos());
        model.addAttribute("cotizaciones", cotizacionService.listarTodas());

        if (placaBuscar != null && !placaBuscar.isBlank()) {
            model.addAttribute("placaBuscada", placaBuscar.toUpperCase().trim());
            model.addAttribute("historialOrdenes", ordenTrabajoService.listarPorPlaca(placaBuscar));
            model.addAttribute("certificacionesPlaca", certificacionService.listarPorPlaca(placaBuscar));
            vehiculoService.buscarPorPlaca(placaBuscar).ifPresent(v -> model.addAttribute("vehiculoSeleccionado", v));
        }

        return "cliente/index";
    }

    @PostMapping("/vehiculos/guardar")
    public String registrarVehiculo(@ModelAttribute Vehiculo vehiculo, RedirectAttributes flash) {
        try {
            if (vehiculo.getClienteId() != null && !vehiculo.getClienteId().isBlank()) {
                clienteService.buscarPorId(vehiculo.getClienteId()).ifPresent(c -> vehiculo.setClienteNombre(c.getNombreCompleto()));
            }
            vehiculoService.guardar(vehiculo);
            flash.addFlashAttribute("mensajeExito", "Vehículo registrado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar vehículo: " + e.getMessage());
        }
        return "redirect:/web/cliente?tab=vehiculos";
    }

    @PostMapping("/clientes/guardar")
    public String registrarCliente(@ModelAttribute Cliente cliente, RedirectAttributes flash) {
        try {
            clienteService.guardar(cliente);
            flash.addFlashAttribute("mensajeExito", "Cliente registrado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar cliente: " + e.getMessage());
        }
        return "redirect:/web/cliente?tab=vehiculos";
    }

    @PostMapping("/cotizaciones/{id}/aprobar")
    public String aprobarCotizacion(@PathVariable String id,
                                    @RequestParam(required = false) String comentario,
                                    RedirectAttributes flash) {
        try {
            cotizacionService.aprobar(id, comentario);
            flash.addFlashAttribute("mensajeExito", "¡Cotización aprobada! Se ha generado la Orden de Trabajo para su vehículo.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al aprobar cotización: " + e.getMessage());
        }
        return "redirect:/web/cliente?tab=cotizaciones";
    }

    @PostMapping("/cotizaciones/{id}/rechazar")
    public String rechazarCotizacion(@PathVariable String id,
                                     @RequestParam(required = false) String motivo,
                                     RedirectAttributes flash) {
        try {
            cotizacionService.rechazar(id, motivo);
            flash.addFlashAttribute("mensajeExito", "Cotización rechazada.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al rechazar cotización: " + e.getMessage());
        }
        return "redirect:/web/cliente?tab=cotizaciones";
    }
}
