package shared;

import java.util.function.Function;

public interface TelemetryInjector extends Function<reactor.util.context.Context, reactor.util.context.Context> {
}
