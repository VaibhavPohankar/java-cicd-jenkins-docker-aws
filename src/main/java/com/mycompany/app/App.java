package com.mycompany.app;

import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class App {

    private static final String MESSAGE = "Welcome to vaibhav Docker hosted jenkins based web APP DEPLOYMENT PIPELINE";

    // ✅ KEEP THIS (fixes your test failure)
    public String getMessage() {
        return MESSAGE;
    }

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            exchange.sendResponseHeaders(200, MESSAGE.length());
            OutputStream os = exchange.getResponseBody();
            os.write(MESSAGE.getBytes());
            os.close();
        });

        server.start();
        System.out.println("Server started on port 8080");
    }
}