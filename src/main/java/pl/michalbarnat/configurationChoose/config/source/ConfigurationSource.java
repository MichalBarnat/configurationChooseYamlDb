package pl.michalbarnat.configurationChoose.config.source;

import java.util.function.Function;

public interface ConfigurationSource {
    <R> R getValue(String key, Function<String, R> valueResolver);
}
