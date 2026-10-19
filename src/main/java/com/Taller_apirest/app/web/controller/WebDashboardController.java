package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import com.Taller_apirest.app.modules.admin.service.ServicioCatalogoService;
import com.Taller_apirest.app.modules.admin.service.UsuarioService;
import com.Taller_apirest.app.modules.certificaciones.service.CertificacionAuditoriaService;
import com.Taller_apirest.app.modules.ordenes.service.CotizacionService;
import com.Taller_apirest.app.modules.ordenes.service.OrdenTrabajoService;
import com.Taller_apirest.app.modules.proveedores.service.PedidoReabastecimientoService;
import com.Taller_apirest.app.modules.vehiculos.service.VehiculoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebDashboardController {

    private final UsuarioService usuarioService;
    private final VehiculoService vehiculoService;
    private final OrdenTrabajoService ordenTrabajoService;
    private final PedidoReabastecimientoService pedidoService;
    private final CertificacionAuditoriaService certificacionService;
    private final ServicioCatalogoService servicioCatalogoService;
    private final RepuestoCatalogoService repuestoCatalogoService;
    private final CotizacionService cotizacionService;

    public WebDashboardController(UsuarioService usuarioService,
                                  VehiculoService vehiculoService,
                                  OrdenTrabajoService ordenTrabajoService,
                                  PedidoReabastecimientoService pedidoService,
                                  CertificacionAuditoriaService certificacionService,
                                  ServicioCatalogoService servicioCatalogoService,
                                  RepuestoCatalogoService repuestoCatalogoService,
                                  CotizacionService cotizacionService) {
        this.usuarioService = usuarioService;
        this.vehiculoService = vehiculoService;
        this.ordenTrabajoService = ordenTrabajoService;
        this.pedidoService = pedidoService;
        this.certificacionService = certificacionService;
        this.servicioCatalogoService = servicioCatalogoService;
        this.repuestoCatalogoService = repuestoCatalogoService;
        this.cotizacionService = cotizacionService;
    }

    @GetMapping("/")
    public String index(Model model) {
        try {
            model.addAttribute("totalUsuarios", usuarioService.listarTodos().size());
            model.addAttribute("totalVehiculos", vehiculoService.listarTodos().size());
            model.addAttribute("totalOrdenes", ordenTrabajoService.listarTodas().size());
            model.addAttribute("totalPedidos", pedidoService.listarTodos().size());
            model.addAttribute("totalCertificaciones", certificacionService.listarTodas().size());
            model.addAttribute("totalServicios", servicioCatalogoService.listarTodos().size());
            model.addAttribute("totalRepuestos", repuestoCatalogoService.listarTodos().size());
            model.addAttribute("totalCotizaciones", cotizacionService.listarTodas().size());

            model.addAttribute("ultimasOrdenes", ordenTrabajoService.listarTodas().stream().limit(5).toList());
            model.addAttribute("ultimasCertificaciones", certificacionService.listarTodas().stream().limit(5).toList());
        } catch (Exception e) {
            // Si la base de datos está iniciando o no hay conexión aún
            model.addAttribute("dbWarning", "La base de datos MongoDB está inicializándose o esperando conexión.");
        }
        return "index";
    }
}
