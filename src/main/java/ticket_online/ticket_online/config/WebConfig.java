package ticket_online.ticket_online.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/api/**") // Menentukan path yang diizinkan untuk CORS
                .allowedOrigins("http://localhost:5173") // Gantilah dengan domain frontend Anda
                .allowedMethods("GET", "POST", "PUT", "DELETE") // Metode HTTP yang diizinkan
//                .allowedHeaders("*") // Header yang diizinkan
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept", "Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers")
                .allowCredentials(true); // Izinkan pengiriman cookie
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploaded-images/**")
                .addResourceLocations("file:./uploaded-images/");
    }


}
