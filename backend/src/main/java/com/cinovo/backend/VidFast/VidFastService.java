package com.cinovo.backend.VidFast;

import com.cinovo.backend.VidFast.Model.Result;
import org.hibernate.validator.constraintvalidators.RegexpURLValidator;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@org.springframework.stereotype.Service
public class VidFastService
{
    private final HttpClient client;

    public VidFastService()
    {
        this.client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.NORMAL).connectTimeout(Duration.ofSeconds(6)).build();
    }

    public Result check(String urlStr)
    {
        Result result = new Result();
        result.setUrl(urlStr);
        try
        {
            URI uri = URI.create(urlStr);
            HttpRequest headReq = HttpRequest.newBuilder(uri).method("HEAD", HttpRequest.BodyPublishers.noBody()).timeout(Duration.ofSeconds(8))
                    .header("User-Agent", "url-checker/1.0").build();
            HttpResponse<Void> headResp;
            try
            {
                headResp = client.send(headReq, HttpResponse.BodyHandlers.discarding());
            }
            catch(IOException | InterruptedException e)
            {
                headResp = null;
            }
            HttpResponse<byte[]> finalResp;
            if(headResp == null || headResp.statusCode() == 405 || headResp.headers().firstValue("content-type").isEmpty())
            {
                HttpRequest getReq =
                        HttpRequest.newBuilder(uri).GET().timeout(Duration.ofSeconds(10)).header("User-Agent", "url-checker/1.0").build();
                finalResp = client.send(getReq, HttpResponse.BodyHandlers.ofByteArray());
                result.setHttpStatus(finalResp.statusCode());
                result.setFinalUrl(finalResp.uri().toString());
            }
            else
            {
                result.setHttpStatus(headResp.statusCode());
                result.setFinalUrl(headResp.uri().toString());
                finalResp = null;
            }
            Map<String, List<String>> headersMap = new HashMap<>();
            if(finalResp != null)
            {
                finalResp.headers().map().forEach(headersMap::put);
            }
            else
            {
                headResp.headers().map().forEach(headersMap::put);
            }
            String xfo = firstIgnoreCase(headersMap, "x-frame-options");
            String csp = firstIgnoreCase(headersMap, "content-security-policy");
            String frameAncestors = parseFrameAncestors(csp);
            boolean blockedByXFO = xfo != null && xfo.toUpperCase(Locale.ROOT).matches("DENY|SAMEORIGIN");
            boolean blockedByCSP = frameAncestors != null && frameAncestors.trim().equalsIgnoreCase("'none'");
            boolean frameAllowed = !(blockedByXFO || blockedByCSP);
            Map<String, String> frameHeaders = new HashMap<>();
            frameHeaders.put("x-frame-options", xfo);
            frameHeaders.put("content-security-policy", csp);
            frameHeaders.put("frame-ancestors", frameAncestors);
            result.setFrameHeaders(frameHeaders);
            result.setFrameAllowed(frameAllowed);
            boolean tlsOk = tlsHandshake(uri.getHost(), 443, 4000);
            result.setTlsOk(tlsOk);
            boolean usable = result.getHttpStatus() == 200 && tlsOk && frameAllowed;
            result.setUsable(usable);
        }
        catch(Exception ex)
        {
            result.setError(ex.getMessage());
            result.setUsable(false);
        }
        return result;
    }

    private static String firstIgnoreCase(Map<String, List<String>> headers, String key)
    {
        for(Map.Entry<String, List<String>> e : headers.entrySet())
        {
            if(e.getKey().equalsIgnoreCase(key))
            {
                List<String> vals = e.getValue();
                return vals.isEmpty() ? null : String.join("; ", vals);
            }
        }
        return null;
    }

    private static String parseFrameAncestors(String csp)
    {
        if(csp == null)
            return null;
        Pattern p = Pattern.compile("frame-ancestors\\s+([^;]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(csp);
        if(m.find())
        {
            return m.group(1).trim();
        }
        return null;
    }

    private static boolean tlsHandshake(String host, int port, int timeoutMs)
    {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try(SSLSocket socket = (SSLSocket) factory.createSocket())
        {
            socket.connect(new java.net.InetSocketAddress(host, port), timeoutMs);
            socket.setSoTimeout(timeoutMs);
            socket.startHandshake();
            return true;
        }
        catch(IOException e)
        {
            return false;
        }
    }
}
