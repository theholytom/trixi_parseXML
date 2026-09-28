package cz.trixi.parsexml.api;

import cz.trixi.parsexml.config.ClientProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private static final Logger log = LoggerFactory.getLogger(ConfigController.class);
    private final ClientProperties properties;

    public ConfigController(ClientProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/url")
    public ResponseEntity<String> getResourceUrl() {
        log.info("Endpoint call: GET /config/url");
        return ResponseEntity.ok(properties.getResourceUrl());
    }

    @PostMapping("/url")
    public ResponseEntity<String> changeResourceUrl(@RequestBody String newUrl) {
        log.info("Endpoint call: POST /config/url (newUrl={})", newUrl);
        properties.setResourceUrl(newUrl);
        return ResponseEntity.accepted()
                .body("New resource URL: " + newUrl);
    }
}
