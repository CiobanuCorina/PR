package com.company;


import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Authenticate {
    URL url = new URL("https://andys.md/ru/login");


    public Authenticate() throws MalformedURLException {
    }

    public List<HttpCookie> getCookies() throws IOException {
        Scanner scanner = new Scanner(System.in);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setInstanceFollowRedirects(true);
        System.out.println("Enter email:");
        String email = scanner.nextLine();
        System.out.println("Enter password:");
        String password = scanner.nextLine();
        String jsonInputString = "{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}";
        OutputStream os = con.getOutputStream();
        os.write(jsonInputString.getBytes(StandardCharsets.UTF_8));
        os.close();
        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        con.getInputStream().close();
        System.out.println(con.getResponseCode());
        String cookiesHeader = con.getHeaderField("Set-Cookie");
        System.out.println(cookiesHeader);
        return HttpCookie.parse(cookiesHeader);
    }
}
