package pl.michalbarnat.configurationChoose.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.configuration.parameters")
public class AppConfigurationProperties {
    private ConfigParameter defaultPageSize;
    private ConfigParameter isApiEnabled;
    private ConfigParameter isWorkingDay;
    private ConfigParameter ownerName;

    @Data
    public static class ConfigParameter {
        private String key;
        private String value;
    }
}
