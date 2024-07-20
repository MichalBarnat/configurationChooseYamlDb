package pl.michalbarnat.configurationChoose.config.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.michalbarnat.configurationChoose.config.AppConfiguration;
import pl.michalbarnat.configurationChoose.config.AppConfigurationProperties;
import pl.michalbarnat.configurationChoose.infra.dba.entity.ConfigurationEntity;
import pl.michalbarnat.configurationChoose.infra.dba.repository.ConfigurationRepository;

import java.util.List;

@Configuration
@EnableConfigurationProperties({AppConfigurationProperties.class})
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationRepository configurationRepository;

    @Getter
    @Value("${app.configuration.source}")
    private String configurationSource;

    @Getter
    @Value("${app.env}")
    private String environment;

    public List<ConfigurationEntity> findAllByEnvironment(String env) {
        return configurationRepository.findAllByEnvironment(env);
    }

    public void saveConfigToDb(List<ConfigurationEntity> entities) {
        configurationRepository.saveAll(entities);
    }

    @Bean
    public AppConfiguration getAppConfiguration(
            AppConfigurationProperties properties
            ) {
        return new AppConfiguration(this, environment, properties, configurationSource);
    }
}
