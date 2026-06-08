package com.uda.hrplatform;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    public static void main(String[] args) throws IOException {
        // Wire all dependencies and register all routes
        AppConfig config = new AppConfig();

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new Routes(config).build());
        server.start();

        System.out.println("Humas Resources Platform is running on http://localhost:8080");
    }
}
