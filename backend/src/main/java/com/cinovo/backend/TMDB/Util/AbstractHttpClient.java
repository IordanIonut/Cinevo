package com.cinovo.backend.TMDB.Util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.jbosslog.JBossLog;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@JBossLog
public abstract class AbstractHttpClient
{
    protected final HttpClient client;
    protected final ApiProperties apiProperties;

    public AbstractHttpClient(ApiProperties apiProperties)
    {
        this.apiProperties = apiProperties;
        this.client = HttpClient.newBuilder().build();
    }

    protected String buildUrlWithParams(String baseUrl, Map<String, String> params)
    {
        if(params == null || params.isEmpty())
            return baseUrl;

        StringBuilder url = new StringBuilder(baseUrl);
        url.append("?");
        params.forEach((key, value) -> {
            if(value != null)
            {
                url.append(URLEncoder.encode(key, StandardCharsets.UTF_8)).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8))
                        .append("&");
            }
        });
        url.deleteCharAt(url.length() - 1);
        return url.toString();
    }

    protected HttpResponse<String> sendGet(String baseUrl, Map<String, String> queryParams) throws Exception
    {
        String fullUrl = buildUrlWithParams(apiProperties.getUrl() + baseUrl, queryParams);
        log.info("Sending GET request to: " + fullUrl);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(fullUrl)).header("Accept", "application/json")
                .header("Authorization", "Bearer " + apiProperties.getKey()).GET().build();

        //        int maxRetries = 15;
        //        int attempt = 0;
        //
        //        while(attempt < maxRetries)
        //        {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(isNull(response.body()))
        {
            return null;
        }
        //to add when is timeout
        //            else if(){
        //                System.out.println("Retryable error detected. Attempt " + (attempt + 1));
        //                Thread.sleep(4000);
        //                log.info(response.body());
        //                attempt++;
        //            }
        else
        {
            return response;
        }

        //        }
        //        throw new RuntimeException("Failed after " + maxRetries + " retries: " + fullUrl);
    }

    protected HttpResponse<String> sendPost(String url, String jsonBody) throws Exception
    {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private boolean isNull(String responseBody)
    {
        try
        {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(responseBody);

            if(root.has("success") && !root.get("success").asBoolean())
            {
                int statusCode = root.path("status_code").asInt(0);
                return statusCode == 34 || statusCode == 7;
            }
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }
}
