package pl.michalbarnat.configurationChoose.infra.dba.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "configurations")
@IdClass(ConfigurationId.class)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationEntity {

    @Id
    @Column(name = "environment")
    private String environment;

    @Id
    @Column(name = "pkey")
    private String key;

    @Column(name = "pvalue")
    private String value;
}
