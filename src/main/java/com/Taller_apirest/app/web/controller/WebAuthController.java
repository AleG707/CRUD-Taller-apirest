package com.Taller_apirest.app.web.controller;

import com.Taller_apirest.app.modules.admin.model.Rol;
import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.modules.admin.service.UsuarioService;
import com.Taller_apirest.app.modules.vehiculos.model.Cliente;
import com.Taller_apirest.app.modules.vehiculos.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

/**
 * Controlador web para autenticación: Login, Logout y Registro de nuevos usuarios.
 * Usa sesión HTTP (HttpSession) como mecanismo de autenticación ligero sin Spring Security.
 */
@Controller
public class WebAuthController {

    public static final String SESSION_USUARIO = "usuarioLogueado";

    private final UsuarioService usuarioService;
    private final ClienteService clienteService;

    public WebAuthController(UsuarioService usuarioService, ClienteService clienteService) {
        this.usuarioService = usuarioService;
        this.clienteService = clienteService;
    }

    // ─── GET /login ────────────────────────────────────────────────────────────
    @GetMapping("/login")
    public String mostrarLogin(HttpSession session, Model model,
                               @RequestParam(required = false) String error,
                               @RequestParam(required = false) String logout) {
        // Si ya está logueado, redirigir al dashboard
        if (session.getAttribute(SESSION_USUARIO) != null) {
            return "redirect:/";
        }
        if (error != null) {
            model.addAttribute("errorLogin", "Correo o contraseña incorrectos. Intenta nuevamente.");
        }
        if (logout != null) {
            model.addAttribute("mensajeLogout", "Sesión cerrada correctamente. ¡Hasta pronto!");
        }
        return "login";
    }

    // ─── POST /login ───────────────────────────────────────────────────────────
    @PostMapping("/login")
    public String procesarLogin(@RequestParam String email,
                                @RequestParam String password,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        Optional<Usuario> optUsuario = usuarioService.buscarPorEmail(email.trim().toLowerCase());

        if (optUsuario.isEmpty()) {
            return "redirect:/login?error";
        }

        Usuario usuario = optUsuario.get();

        // Verificar contraseña (texto plano por ahora; fácilmente reemplazable con BCrypt)
        if (!usuario.getPassword().equals(password)) {
            return "redirect:/login?error";
        }

        if (!usuario.isActivo()) {
            redirectAttributes.addFlashAttribute("errorLogin", "Tu cuenta está desactivada. Contacta al administrador.");
            return "redirect:/login";
        }

        // Guardar usuario en sesión
        session.setAttribute(SESSION_USUARIO, usuario);
        session.setMaxInactiveInterval(60 * 60); // 1 hora

        // Redirigir según rol
        return switch (usuario.getRol()) {
            case ADMINISTRADOR -> "redirect:/web/admin";
            case MECANICO      -> "redirect:/web/mecanico";
            case CLIENTE       -> "redirect:/web/cliente";
            case PROVEEDOR     -> "redirect:/web/proveedor";
            case AUDITOR       -> "redirect:/web/auditor";
        };
    }

    // ─── GET /logout ───────────────────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    // ─── GET /registro ─────────────────────────────────────────────────────────
    @GetMapping("/registro")
    public String mostrarRegistro(HttpSession session, Model model) {
        if (session.getAttribute(SESSION_USUARIO) != null) {
            return "redirect:/";
        }
        model.addAttribute("roles", Rol.values());
        return "registro";
    }

    // ─── POST /registro ────────────────────────────────────────────────────────
    @PostMapping("/registro")
    public String procesarRegistro(@RequestParam String nombre,
                                   @RequestParam String email,
                                   @RequestParam String password,
                                   @RequestParam String confirmarPassword,
                                   @RequestParam String telefono,
                                   @RequestParam Rol rol,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        model.addAttribute("roles", Rol.values());

        // Validaciones
        if (!password.equals(confirmarPassword)) {
            model.addAttribute("errorRegistro", "Las contraseñas no coinciden.");
            return "registro";
        }
        if (password.length() < 6) {
            model.addAttribute("errorRegistro", "La contraseña debe tener al menos 6 caracteres.");
            return "registro";
        }
        String emailNorm = email.trim().toLowerCase();
        if (usuarioService.buscarPorEmail(emailNorm).isPresent()) {
            model.addAttribute("errorRegistro", "Ya existe un usuario registrado con ese correo electrónico.");
            return "registro";
        }

        // Crear y guardar el nuevo usuario
        Usuario nuevo = new Usuario();
        nuevo.setNombre(nombre.trim());
        nuevo.setEmail(emailNorm);
        nuevo.setPassword(password);
        nuevo.setTelefono(telefono.trim());
        nuevo.setRol(rol);
        nuevo.setActivo(true);
        usuarioService.guardar(nuevo);

        // Si se registra como CLIENTE, sincronizar también en taller_clientes_db
        if (rol == Rol.CLIENTE) {
            try {
                if (clienteService.buscarPorEmail(emailNorm).isEmpty()) {
                    Cliente cli = new Cliente();
                    cli.setNombreCompleto(nombre.trim());
                    cli.setEmail(emailNorm);
                    cli.setTelefono(telefono.trim());
                    cli.setIdentificacion("CC-" + System.currentTimeMillis() % 100000000);
                    cli.setDireccion("No especificada");
                    clienteService.guardar(cli);
                }
            } catch (Exception ignored) {
                // Si la base de datos de clientes no está disponible o falla, el usuario ya quedó registrado
            }
        }

        redirectAttributes.addFlashAttribute("mensajeExito",
                "¡Registro exitoso! Bienvenido/a, " + nombre.split(" ")[0] + ". Inicia sesión con tus credenciales.");
        return "redirect:/login";
    }
}
