package serviceb;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Duration;

@SpringBootApplication
public class ServiceApplication {
    public static void main(final String... args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @RestController
    class Endpoint {
        private static final Logger log = LoggerFactory.getLogger(Endpoint.class);

        private final DaprClient daprClient;

        public Endpoint(final DaprClient daprClient) {
            this.daprClient = daprClient;
        }

        @PostMapping("/endpoint")
        ResponseEntity<Void> endpoint(@RequestBody final String data) {
            log.info("Service B handling request {}", data);
            var result = daprClient.invokeMethod(
                    "service-c",
                    "endpoint",
                    "hello from service b",
                    HttpExtension.POST,
                    Void.class
            );
            result.block(Duration.ofMillis(250));

            return ResponseEntity.accepted().build();
        }
    }

    @Configuration
    class ServiceConfiguration {
        @Bean
        public DaprClient daprClient() {
            return new DaprClientBuilder().build();
        }
    }
}