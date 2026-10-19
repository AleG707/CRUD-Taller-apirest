package com.Taller_apirest.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración MVC: registra el interceptor de autenticación por sesión.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    public WebMvcConfig(AuthInterceptor authInterceptor) {
        this.authInterceptor = authInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/web/**")   // Proteger todas las rutas de rol
                .excludePathPatterns(
                    "/login", "/logout", "/registro",
                    "/css/**", "/js/**", "/images/**",
                    "/swagger-ui/**", "/v3/api-docs/**",
                    "/webjars/**"
                );
    }
}
