package cz.trixi.parsexml.job;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class UrlSourceConnector {

    private final RestClient restClient;

    public UrlSourceConnector(RestClient restClient) {
        this.restClient = restClient;
    }

    public byte[] callApiEndpoint() {
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
}