package pl.michalbarnat.configurationChoose.infra.dba.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.michalbarnat.configurationChoose.infra.dba.entity.ConfigurationEntity;
import pl.michalbarnat.configurationChoose.infra.dba.entity.ConfigurationId;

import java.util.List;

public interface ConfigurationRepository extends JpaRepository<ConfigurationEntity, ConfigurationId> {

    List<ConfigurationEntity> findAllByEnvironment(String env);

}
