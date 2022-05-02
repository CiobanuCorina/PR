package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Requests {
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("88.159.152.98", 80));

    public HttpURLConnection setConnectionProperties(String requestMethod, URL url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        return con;
    }

    public String getCats() throws IOException {
        URL url = new URL("https://api.thecatapi.com/v1/breeds?attach_breed=0");
        HttpURLConnection con = setConnectionProperties("GET", url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        return in.readLine();
    }

    public void voteSpecificCats(List<CatInfo> cats) {
        final ExecutorService executor = Executors.newFixedThreadPool(5);
        final List<Future<?>> futures = new ArrayList<>();
        for (CatInfo cat : cats) {
            Future<?> future = executor.submit(() -> {
                try {
                    URL url = new URL("https://api.thecatapi.com/v1/votes");
                    HttpURLConnection con = setConnectionProperties("POST", url);
                    con.setRequestProperty("x-api-key", "c999a17e-b7c5-4dc0-ab8c-9e85477301bf");
                    con.setDoOutput(true);
                    String jsonCat = "{\"image_id\": \"" + cat.getImage().getId() + "\", \"sub_id\": \"demo-677e3\", \"value\": true}";
                    OutputStream os = con.getOutputStream();
                    os.write(jsonCat.getBytes(StandardCharsets.UTF_8));
                    os.close();
                    System.out.println(con.getResponseCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            futures.add(future);
        }
        executor.shutdown();
        try {
            for (Future<?> future : futures) {
                future.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> headRequest() throws IOException {
        URL url = new URL("https://api.thecatapi.com/v1/votes");
        HttpURLConnection con = setConnectionProperties("HEAD", url);
        con.setRequestProperty("x-api-key", "c999a17e-b7c5-4dc0-ab8c-9e85477301bf");
        System.out.println(con.getResponseCode());
        return con.getHeaderFields();
    }
    public Map<String, List<String>> optionsRequest() throws IOException {
        URL url = new URL("https://api.thecatapi.com");
        HttpURLConnection con = setConnectionProperties("OPTIONS", url);
        System.out.println(con.getResponseCode());
        return con.getHeaderFields();
    }
}
