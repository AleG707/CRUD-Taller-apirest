package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.proveedores.model.PedidoReabastecimiento;
import com.Taller_apirest.app.modules.proveedores.model.Proveedor;
import com.Taller_apirest.app.modules.proveedores.model.SuministroProveedor;
import com.Taller_apirest.app.modules.proveedores.service.PedidoReabastecimientoService;
import com.Taller_apirest.app.modules.proveedores.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/proveedor")
public class WebProveedorController {

    private final ProveedorService proveedorService;
    private final PedidoReabastecimientoService pedidoService;

    public WebProveedorController(ProveedorService proveedorService,
                                  PedidoReabastecimientoService pedidoService) {
        this.proveedorService = proveedorService;
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "pedidos") String tab) {
        model.addAttribute("tabActiva", tab);
        model.addAttribute("proveedores", proveedorService.listarTodos());
        model.addAttribute("pedidos", pedidoService.listarTodos());
        return "proveedor/index";
    }

    @PostMapping("/pedidos/{id}/despachar")
    public String despacharPedido(@PathVariable String id,
                                  @RequestParam String transportadora,
                                  @RequestParam String numeroGuia,
                                  RedirectAttributes flash) {
        try {
            pedidoService.despachar(id, transportadora, numeroGuia);
            flash.addFlashAttribute("mensajeExito", "Pedido despachado con número de guía: " + numeroGuia);
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al despachar pedido: " + e.getMessage());
        }
        return "redirect:/web/proveedor?tab=pedidos";
    }

    @PostMapping("/pedidos/{id}/entregar")
    public String confirmarEntrega(@PathVariable String id,
                                   @RequestParam(required = false) String observaciones,
                                   RedirectAttributes flash) {
        try {
            pedidoService.registrarEntrega(id, observaciones);
            flash.addFlashAttribute("mensajeExito", "Entrega confirmada y stock del taller reabastecido automáticamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar entrega: " + e.getMessage());
        }
        return "redirect:/web/proveedor?tab=pedidos";
    }

    @PostMapping("/suministros/guardar")
    public String agregarSuministro(@RequestParam String proveedorId,
                                    @ModelAttribute SuministroProveedor suministro,
                                    RedirectAttributes flash) {
        try {
            proveedorService.agregarSuministro(proveedorId, suministro);
            flash.addFlashAttribute("mensajeExito", "Nuevo repuesto agregado al catálogo mayorista del proveedor.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al agregar suministro: " + e.getMessage());
        }
        return "redirect:/web/proveedor?tab=inventario";
    }

    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor, RedirectAttributes flash) {
        try {
            proveedorService.guardar(proveedor);
            flash.addFlashAttribute("mensajeExito", "Proveedor registrado exitosamente.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al registrar proveedor: " + e.getMessage());
        }
        return "redirect:/web/proveedor?tab=inventario";
    }
}
