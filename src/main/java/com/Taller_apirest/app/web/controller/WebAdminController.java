package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.admin.model.RepuestoCatalogo;
import com.Taller_apirest.app.modules.admin.model.Rol;
import com.Taller_apirest.app.modules.admin.model.ServicioCatalogo;
import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.modules.admin.service.RepuestoCatalogoService;
import com.Taller_apirest.app.modules.admin.service.ServicioCatalogoService;
import com.Taller_apirest.app.modules.admin.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/admin")
public class WebAdminController {

    private final UsuarioService usuarioService;
    private final ServicioCatalogoService servicioService;
    private final RepuestoCatalogoService repuestoService;

    public WebAdminController(UsuarioService usuarioService,
                              ServicioCatalogoService servicioService,
                              RepuestoCatalogoService repuestoService) {
        this.usuarioService = usuarioService;
        this.servicioService = servicioService;
        this.repuestoService = repuestoService;
    }

    @GetMapping
    public String index(Model model, @RequestParam(defaultValue = "usuarios") String tab) {
        model.addAttribute("tabActiva", tab);
        model.addAttribute("usuarios", usuarioService.listarTodos());
        model.addAttribute("servicios", servicioService.listarTodos());
        model.addAttribute("repuestos", repuestoService.listarTodos());
        model.addAttribute("roles", Rol.values());
        return "admin/index";
    }

    // --- CRUD Usuarios ---
    @PostMapping("/usuarios/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes flash) {
        try {
            if (usuario.getId() != null && !usuario.getId().isBlank()) {
                usuarioService.actualizar(usuario.getId(), usuario);
                flash.addFlashAttribute("mensajeExito", "Usuario actualizado correctamente.");
            } else {
                usuarioService.guardar(usuario);
                flash.addFlashAttribute("mensajeExito", "Usuario creado exitosamente.");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al procesar usuario: " + e.getMessage());
        }
        return "redirect:/web/admin?tab=usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuario(@PathVariable String id, RedirectAttributes flash) {
        try {
            usuarioService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Usuario eliminado del sistema.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al eliminar usuario.");
        }
        return "redirect:/web/admin?tab=usuarios";
    }

    // --- CRUD Servicios ---
    @PostMapping("/servicios/guardar")
    public String guardarServicio(@ModelAttribute ServicioCatalogo servicio, RedirectAttributes flash) {
        try {
            if (servicio.getId() != null && !servicio.getId().isBlank()) {
                servicioService.actualizar(servicio.getId(), servicio);
                flash.addFlashAttribute("mensajeExito", "Servicio actualizado correctamente.");
            } else {
                servicioService.guardar(servicio);
                flash.addFlashAttribute("mensajeExito", "Servicio añadido al catálogo.");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al procesar servicio: " + e.getMessage());
        }
        return "redirect:/web/admin?tab=servicios";
    }

    @GetMapping("/servicios/eliminar/{id}")
    public String eliminarServicio(@PathVariable String id, RedirectAttributes flash) {
        try {
            servicioService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Servicio eliminado del catálogo.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al eliminar servicio.");
        }
        return "redirect:/web/admin?tab=servicios";
    }

    // --- CRUD Repuestos ---
    @PostMapping("/repuestos/guardar")
    public String guardarRepuesto(@ModelAttribute RepuestoCatalogo repuesto, RedirectAttributes flash) {
        try {
            if (repuesto.getId() != null && !repuesto.getId().isBlank()) {
                repuestoService.actualizar(repuesto.getId(), repuesto);
                flash.addFlashAttribute("mensajeExito", "Repuesto actualizado en catálogo.");
            } else {
                repuestoService.guardar(repuesto);
                flash.addFlashAttribute("mensajeExito", "Repuesto añadido al catálogo.");
            }
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al procesar repuesto: " + e.getMessage());
        }
        return "redirect:/web/admin?tab=repuestos";
    }

    @GetMapping("/repuestos/eliminar/{id}")
    public String eliminarRepuesto(@PathVariable String id, RedirectAttributes flash) {
        try {
            repuestoService.eliminar(id);
            flash.addFlashAttribute("mensajeExito", "Repuesto eliminado del catálogo.");
        } catch (Exception e) {
            flash.addFlashAttribute("mensajeError", "Error al eliminar repuesto.");
        }
        return "redirect:/web/admin?tab=repuestos";
    }
}
