package cz.trixi.parsexml.job;

import cz.trixi.parsexml.config.ClientProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;

@Component
public class UrlSourceConnector {

    private final ClientProperties properties;
    private final RestClient.Builder builder;
    private RestClient restClient;
    private String baseUrl;

    public UrlSourceConnector(ClientProperties properties, RestClient.Builder builder) {
        this.properties = properties;
        this.builder = builder;
    }

    public byte[] callApiEndpoint() {
        restClient();

        byte[] data = restClient.get()
                .header("Accept", "application/zip")
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new IllegalStateException("HTTP error: " + res.getStatusCode());
                })
                .body(byte[].class);

        if (data == null || data.length == 0) {
            throw new IllegalStateException("Empty ZIP payload received from API");
        }

        return data;
    }

    public void restClient() {
        if (restClient != null && baseUrl.equals(properties.getResourceUrl())) {
            return;
        }
        baseUrl = properties.getResourceUrl();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout((int) properties.getConnectTimeout().toMillis());
        requestFactory.setReadTimeout((int) properties.getReadTimeout().toMillis());
        restClient = builder.baseUrl(baseUrl).requestFactory(requestFactory).build();
    }
}