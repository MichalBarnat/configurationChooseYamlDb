package pl.michalbarnat.configurationChoose.config.source;

import pl.michalbarnat.configurationChoose.config.AppConfigurationProperties;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class YamlConfigurationSource implements ConfigurationSource {
    private final Map<String, String> propertiesMap;

    public YamlConfigurationSource(AppConfigurationProperties properties) {
        this.propertiesMap = new HashMap<>();
        loadProperties(properties);
    }

    private void loadProperties(AppConfigurationProperties properties) {
        Field[] fields = AppConfigurationProperties.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            try {
                AppConfigurationProperties.ConfigParameter parameter =
                        (AppConfigurationProperties.ConfigParameter) field.get(properties);
                if (parameter != null && parameter.getKey() != null) {
                    propertiesMap.put(parameter.getKey(), parameter.getValue());
                }
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Unable to access property", e);
            }
        }
    }

    @Override
    public <R> R getValue(String key, Function<String, R> valueResolver) {
        String value = propertiesMap.get(key);
        return valueResolver.apply(value);
    }
}
