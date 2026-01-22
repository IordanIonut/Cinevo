package com.cinovo.backend.DB.Util;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.util.HexFormat;

public class CompareImage
{

    private static final HttpClient http = HttpClient.newBuilder().build();

    public static String sha256OfUrl(String url) throws Exception
    {
        HttpRequest req = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpResponse<InputStream> resp = http.send(req, HttpResponse.BodyHandlers.ofInputStream());
        if(resp.statusCode() != 200)
            throw new RuntimeException("Failed to fetch: " + resp.statusCode());
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        try(InputStream in = resp.body())
        {
            byte[] buffer = new byte[8192];
            int read;
            while((read = in.read(buffer)) != -1)
            {
                md.update(buffer, 0, read);
            }
        }
        byte[] digest = md.digest();
        return HexFormat.of().formatHex(digest);
    }

    public static void main(String[] args) throws Exception
    {
        String a = "http://image.tmdb.org/t/p/original/6tpAPeuuqbVnYWWPoOLEDLSBU7a.jpg";
        String b = "http://image.tmdb.org/t/p/original/ctU9S47MoJDN9CB7SCaitcfyyIu.jpg";
        String ha = sha256OfUrl(a);
        String hb = sha256OfUrl(b);
        System.out.println("A: " + ha);
        System.out.println("B: " + hb);
        System.out.println("Identical bytes: " + ha.equals(hb));
    }

}