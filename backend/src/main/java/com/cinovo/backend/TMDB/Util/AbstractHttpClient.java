package com.cinovo.backend.TMDB.Util;

import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class AbstractHttpClient {
    protected final HttpClient client;
    protected final ApiProperties apiProperties;

    public AbstractHttpClient(ApiProperties apiProperties) {
        this.apiProperties = apiProperties;
        this.client = HttpClient.newBuilder().build();
    }

    protected String buildUrlWithParams(String baseUrl, Map<String, String> params) {
        if (params == null || params.isEmpty()) return baseUrl;

        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        params.forEach((key, value) -> {
            url.append(URLEncoder.encode(key, StandardCharsets.UTF_8));
            url.append("=");
            url.append(URLEncoder.encode(value, StandardCharsets.UTF_8));
            url.append("&");
        });
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    protected HttpResponse<String> sendGet(String baseUrl, Map<String, String> queryParams) throws Exception {
        System.out.println("Sending GET request to: " + apiProperties.getUrl() + baseUrl);
        String fullUrl = buildUrlWithParams(apiProperties.getUrl() + baseUrl, queryParams);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(fullUrl))
                .header("Accept", "application/json")
                .header("Authorization", "Bearer " + apiProperties.getKey())
                .GET()
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    protected HttpResponse<String> sendPost(String url, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
