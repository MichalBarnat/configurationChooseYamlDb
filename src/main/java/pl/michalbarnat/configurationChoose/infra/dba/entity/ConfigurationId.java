package pl.michalbarnat.configurationChoose.infra.dba.entity;

import java.io.Serializable;
import java.util.Objects;

public class ConfigurationId implements Serializable {
    private String environment;
    private String key;

    public ConfigurationId() {

    }

    public ConfigurationId(String environment, String key) {
        this.environment = environment;
        this.key = key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConfigurationId that = (ConfigurationId) o;
        return Objects.equals(environment, that.environment) && Objects.equals(key, that.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(environment, key);
    }
}
