package br.com.raizesdonordeste.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI().info(new Info()
                .title("API Raízes do Nordeste")
                .version("1.0.0")
                .description("""
                        Back-end da rede de lanchonetes Raízes do Nordeste (Projeto Multidisciplinar — Trilha Back-end).
                        API única que atende todos os canais do estudo de caso: aplicativo oficial, totens de
                        autoatendimento, balcão e pick-up. Pagamentos são processados por serviço externo
                        (o sistema apenas solicita e recebe o resultado via callback)."""));
    }
}
