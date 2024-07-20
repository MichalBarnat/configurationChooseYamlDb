package pl.michalbarnat.configurationChoose.config;

import lombok.extern.slf4j.Slf4j;
import pl.michalbarnat.configurationChoose.config.service.ConfigurationService;
import pl.michalbarnat.configurationChoose.config.source.ConfigurationSource;
import pl.michalbarnat.configurationChoose.config.source.DatabaseConfigurationSource;
import pl.michalbarnat.configurationChoose.config.source.YamlConfigurationSource;
import pl.michalbarnat.configurationChoose.infra.dba.entity.ConfigurationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static pl.michalbarnat.configurationChoose.config.util.DefaultValues.DEFAULT_PAGE_SIZE;
import static pl.michalbarnat.configurationChoose.config.util.DefaultValues.IS_API_ENABLED;
import static pl.michalbarnat.configurationChoose.config.util.DefaultValues.IS_WORKING_DAY;
import static pl.michalbarnat.configurationChoose.config.util.DefaultValues.OWNER_NAME;

@Slf4j
public class AppConfiguration {
    private final ConfigurationService configurationService;
    private final String env;
    private final AppConfigurationProperties properties;
    private final ConfigurationSource configSource;

    List<ConfigurationEntity> items;

    public AppConfiguration(
            ConfigurationService configurationService,
            String env,
            AppConfigurationProperties properties,
            String configurationSource) {
        this.configurationService = configurationService;
        this.env = env;
        this.properties = properties;
        this.configSource = createConfigurationSource(configurationSource);

        reload();
        init();
    }

    public String getOwnerName() {
        return getValue(properties.getOwnerName().getKey(), Function.identity());
    }

    public int getDefaultPageSize() {
        return getValue(properties.getDefaultPageSize().getKey(), Integer::valueOf);
    }

    public boolean getIsApiEnabled() {
        return getValue(properties.getIsApiEnabled().getKey(), v -> v != null && v.equals("1"));
    }

    public boolean getIsWorkingDay() {
        return getValue(properties.getIsWorkingDay().getKey(), v -> v != null && v.equals("1"));
    }

    public Map<String, Object> getCurrentConfiguration() {
        Map<String, Object> config = new HashMap<>();
        config.put("DEFAULT_PAGE_SIZE", getDefaultPageSize());
        config.put("IS_API_ENABLED", getIsApiEnabled());
        config.put("IS_WORKING_DAY", getIsWorkingDay());
        config.put("OWNER_NAME", getOwnerName());
        return config;
    }

    public void reload() {
        if (configSource instanceof DatabaseConfigurationSource dcs) {
            items = new ArrayList<>(configurationService.findAllByEnvironment(env));
            dcs.setItems(items);
            log.info("Configuration reloaded");
        }
    }

    private <R> R getValue(String propertyName, Function<String, R> valueResolver) {
        return configSource.getValue(propertyName, valueResolver);
    }

    private void init() {
        if (configSource instanceof DatabaseConfigurationSource) {
            log.info("init configuration");
            initProperty(properties.getDefaultPageSize().getKey(), DEFAULT_PAGE_SIZE);
            initProperty(properties.getIsApiEnabled().getKey(), IS_API_ENABLED);
            initProperty(properties.getIsWorkingDay().getKey(), IS_WORKING_DAY);
            initProperty(properties.getOwnerName().getKey(), OWNER_NAME);

            configurationService.saveConfigToDb(items);
        }
    }

    private void initProperty(String propertyName, String defaultValue) {
        if (isPropertyNotDefined(propertyName)) {
            ConfigurationEntity entity = new ConfigurationEntity();
            entity.setEnvironment(this.env);
            entity.setKey(propertyName);
            entity.setValue(defaultValue);

            this.items.add(entity);
        }
    }

    private boolean isPropertyNotDefined(String propertyName) {
        return this.items.stream().noneMatch(item -> item.getKey().equals(propertyName));
    }

    private ConfigurationSource createConfigurationSource(String configurationSource) {
        return switch (configurationSource) {
            case "database" -> new DatabaseConfigurationSource(this.items);
            case "yaml" -> new YamlConfigurationSource(this.properties);
            default -> throw new IllegalArgumentException(
                    "Wrong configuration source value! Allowed: yaml or database.");
        };
    }


}
