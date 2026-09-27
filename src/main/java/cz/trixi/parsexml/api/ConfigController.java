package cz.trixi.parsexml.api;

import cz.trixi.parsexml.config.ClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ClientProperties properties;

    public ConfigController(ClientProperties properties) {
        this.properties = properties;
    }

    @GetMapping("/url")
    public ResponseEntity<String> getResourceUrl() {
        return ResponseEntity.ok(properties.getResourceUrl());
    }

    @PostMapping("/url")
    public ResponseEntity<String> changeResourceUrl(@RequestBody String newUrl) {
        properties.setResourceUrl(newUrl);
        return ResponseEntity.accepted()
                .body("New resource URL: " + newUrl);
    }
}
