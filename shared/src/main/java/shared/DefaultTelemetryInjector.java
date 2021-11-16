package shared;

import io.opentelemetry.api.OpenTelemetry;
import reactor.util.context.Context;

import java.util.HashMap;
import java.util.Map;

public class DefaultTelemetryInjector implements TelemetryInjector {
    private final OpenTelemetry openTelemetry;

    public DefaultTelemetryInjector(final OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
    }

    @Override
    public Context apply(final reactor.util.context.Context context) {
        final Map<String, String> map = extractTelemetryContext();
        return context.putAll(Context.of(map).readOnly());
    }

    private Map<String, String> extractTelemetryContext() {
        final Map<String, String> map = new HashMap<>();
        openTelemetry.getPropagators().getTextMapPropagator()
                .inject(io.opentelemetry.context.Context.current(), map, (carrier, key, value) -> {
                    map.put(key, value);
                });
        return map;
    }
}
