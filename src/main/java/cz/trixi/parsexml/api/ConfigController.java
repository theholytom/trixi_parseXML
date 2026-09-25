package cz.trixi.parsexml.api;

import cz.trixi.parsexml.config.ClientConfig;
import cz.trixi.parsexml.config.ClientProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("config")
public class ConfigController {

    private final ClientProperties properties;
    private final ClientConfig config;
    private final RestClient.Builder builder;

    public ConfigController(ClientProperties properties, ClientConfig config, RestClient.Builder builder) {
        this.properties = properties;
        this.config = config;
        this.builder = builder;
    }

    @GetMapping("/url")
    public ResponseEntity<String> getResourceUrl() {
        return ResponseEntity.ok(properties.getResourceUrl());
    }

    @PostMapping("/url")
    public ResponseEntity<String> changeResourceUrl(@RequestBody String newUrl) {
        properties.setResourceUrl(newUrl);
        config.restClient(builder, properties);
        return ResponseEntity.accepted()
                .body("New resource URL: " + newUrl);
    }
}
