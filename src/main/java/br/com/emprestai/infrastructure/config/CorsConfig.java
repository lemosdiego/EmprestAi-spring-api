package br.com.emprestai.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Aplica a todas as rotas
                .allowedOrigins(
                        "https://emprestai.netlify.app", // Seu frontend em produção
                        "http://localhost:3000",         // Seu frontend local (React/Next/Vue)
                        "http://localhost:4200",         // Seu frontend local (Angular)
                        "http://localhost:5173"          // Seu frontend local (Vite)
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "TRACE", "CONNECT");
    }
}
