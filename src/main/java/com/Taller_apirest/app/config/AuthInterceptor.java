package com.Taller_apirest.app.config;

import com.Taller_apirest.app.modules.admin.model.Usuario;
import com.Taller_apirest.app.web.controller.WebAuthController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * Interceptor que protege todas las rutas /web/** requiriendo sesión activa.
 * Si el usuario no está logueado, lo redirige a /login.
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    // Rutas que NO requieren autenticación
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

        // Para rutas /web/**, verificar sesión
        if (uri.startsWith("/web/")) {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(WebAuthController.SESSION_USUARIO) == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }

            // Pasar datos del usuario a todos los requests para usar en templates
            Usuario usuario = (Usuario) session.getAttribute(WebAuthController.SESSION_USUARIO);
            request.setAttribute("usuarioSesion", usuario);
        }

        return true;
    }
}
