package pl.michalbarnat.configurationChoose.config.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.michalbarnat.configurationChoose.config.AppConfiguration;
import pl.michalbarnat.configurationChoose.config.service.ConfigurationService;

import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
public class ConfigController {

    private final AppConfiguration appConfiguration;
    private final ConfigurationService configurationService;

    @PostMapping("/reload")
    public ResponseEntity<String> reloadFxConfig() {
        switch (configurationService.getConfigurationSource()) {
            case "database":
                appConfiguration.reload();
                return ResponseEntity.ok("Config reloaded [updated from database]");
            case "yaml":
                return ResponseEntity.status(BAD_REQUEST).body("You cant reload configuration from yaml!");
            default:
                return ResponseEntity.status(BAD_REQUEST).body("Wrong value for fx.configuration.source!");
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getConfiguration() {
        return ResponseEntity.ok(appConfiguration.getCurrentConfiguration());
    }

}
