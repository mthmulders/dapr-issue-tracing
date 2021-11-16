package serviceb;

import io.dapr.client.DaprClient;
import io.dapr.client.DaprClientBuilder;
import io.dapr.client.domain.HttpExtension;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.instrumentation.spring.autoconfigure.EnableOpenTelemetry;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.autoconfigure.AutoConfiguredOpenTelemetrySdk;
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
import shared.DefaultTelemetryInjector;
import shared.TelemetryInjector;

import java.time.Duration;

@EnableOpenTelemetry
@SpringBootApplication
public class ServiceApplication {
    public static void main(final String... args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

    @RestController
    class Endpoint {
        private static final Logger log = LoggerFactory.getLogger(Endpoint.class);

        private final DaprClient daprClient;
        private final TelemetryInjector telemetryInjector;

        public Endpoint(final DaprClient daprClient, final TelemetryInjector telemetryInjector) {
            this.daprClient = daprClient;
            this.telemetryInjector = telemetryInjector;
        }

        @PostMapping("/endpoint")
        ResponseEntity<Void> endpoint(@RequestBody final String data) {
            log.info("[{}] Service B handling request {}", Span.current().getSpanContext().getTraceId(), data);

            var result = daprClient.invokeMethod(
                    "service-c",
                    "endpoint",
                    "hello from service b",
                    HttpExtension.POST,
                    Void.class
            ).contextWrite(telemetryInjector);
            result.block(Duration.ofMillis(500));

            return ResponseEntity.accepted().build();
        }
    }

    @Configuration
    static class ServiceConfiguration {
        @Bean
        public DaprClient daprClient() {
            return new DaprClientBuilder().build();
        }

        @Bean
        public OpenTelemetrySdk openTelemetrySdk() {
            return AutoConfiguredOpenTelemetrySdk.initialize().getOpenTelemetrySdk();
        }

        @Bean
        public TelemetryInjector telemetryInjector(final OpenTelemetry openTelemetry) {
            return new DefaultTelemetryInjector(openTelemetry);
        }
    }
}
