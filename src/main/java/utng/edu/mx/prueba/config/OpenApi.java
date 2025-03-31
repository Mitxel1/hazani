package utng.edu.mx.prueba.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class OpenApi {

        @Bean
        public OpenAPI openAPI() {
            return new OpenAPI()
                    .addServersItem(new Server().url("http://localhost:8035/swagger-ui/index.html#/"));

        }
}
