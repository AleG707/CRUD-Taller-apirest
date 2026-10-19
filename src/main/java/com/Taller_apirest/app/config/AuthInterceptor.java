package com.Taller_apirest.app.config;

import com.Taller_apirest.app.modules.admin.model.Rol;
import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.web.controller.WebAuthController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor que protege todas las rutas /web/** requiriendo sesión activa
 * y validando el control de acceso basado en roles (RBAC).
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private static final String[] RUTAS_PUBLICAS = {
        "/login", "/logout", "/registro", "/", "/swagger-ui", "/v3/api-docs",
        "/css/", "/js/", "/images/", "/webjars/"
    };

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                             Object handler) throws Exception {
        String uri = request.getRequestURI();

        // Permitir rutas públicas
        for (String ruta : RUTAS_PUBLICAS) {
            if (uri.startsWith(ruta)) {
                return true;
            }
        }

        // Para rutas /web/**, verificar sesión activa
        if (uri.startsWith("/web/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(WebAuthController.SESSION_USUARIO) == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }

            Usuario usuario = (Usuario) session.getAttribute(WebAuthController.SESSION_USUARIO);
            request.setAttribute("usuarioSesion", usuario);

            // El Administrador tiene acceso total a todos los módulos
            if (usuario.getRol() == Rol.ADMINISTRADOR) {
                return true;
            }

            // Control de acceso por rol específico
            if (uri.startsWith("/web/admin") && usuario.getRol() != Rol.ADMINISTRADOR) {
                redirigirSegunRol(response, request, usuario.getRol());
                return false;
            }
            if (uri.startsWith("/web/mecanico") && usuario.getRol() != Rol.MECANICO) {
                redirigirSegunRol(response, request, usuario.getRol());
                return false;
            }
            if (uri.startsWith("/web/cliente") && usuario.getRol() != Rol.CLIENTE) {
                redirigirSegunRol(response, request, usuario.getRol());
                return false;
            }
            if (uri.startsWith("/web/proveedor") && usuario.getRol() != Rol.PROVEEDOR) {
                redirigirSegunRol(response, request, usuario.getRol());
                return false;
            }
            if (uri.startsWith("/web/auditor") && usuario.getRol() != Rol.AUDITOR) {
                redirigirSegunRol(response, request, usuario.getRol());
                return false;
            }
        }

        return true;
    }

    private void redirigirSegunRol(HttpServletResponse response, HttpServletRequest request, Rol rol) throws Exception {
        String destino = switch (rol) {
            case ADMINISTRADOR -> "/web/admin";
            case MECANICO      -> "/web/mecanico";
            case CLIENTE       -> "/web/cliente";
            case PROVEEDOR     -> "/web/proveedor";
            case AUDITOR       -> "/web/auditor";
        };
        response.sendRedirect(request.getContextPath() + destino);
    }
}
