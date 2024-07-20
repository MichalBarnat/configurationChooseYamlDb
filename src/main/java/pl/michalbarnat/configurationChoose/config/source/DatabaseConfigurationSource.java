package pl.michalbarnat.configurationChoose.config.source;

import lombok.Setter;
import pl.michalbarnat.configurationChoose.infra.dba.entity.ConfigurationEntity;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Setter
public class DatabaseConfigurationSource implements ConfigurationSource {

    private List<ConfigurationEntity> items;

    public DatabaseConfigurationSource(List<ConfigurationEntity> items) {
        this.items = items;
    }


    @Override
    public <R> R getValue(String key, Function<String, R> valueResolver) {
        Optional<ConfigurationEntity> item = items.stream()
                .filter(i -> i != null && i.getKey() != null && i.getKey().equals(key))
                .findFirst();

        String value = item.map(ConfigurationEntity::getValue).orElse(null);
        return valueResolver.apply(value);
    }
}
