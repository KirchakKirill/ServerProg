import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class Main {

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8089), 0);


        server.createContext("/", new AsyncHandler());


        server.start();
        System.out.println("Server started on port 8089");
    }
}

class AsyncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) {
        String requestMethod = exchange.getRequestMethod();
        String requestedPath = exchange.getRequestURI().getPath();

        CompletableFuture.runAsync(() -> logRequest(exchange));

        System.out.println("handle method 1 ");
        if (requestMethod.equalsIgnoreCase("GET")) {
            File file = new File("." + requestedPath);
            if (file.exists() && file.isFile()) {
                CompletableFuture.runAsync(() -> sendFileResponse(exchange, file));
                System.out.println("handle method 2 ");
            } else {
                System.out.println("handle method 3 ");
                CompletableFuture.runAsync(() -> send404Response(exchange));
            }
        } else {
            System.out.println("handle method 4 ");
            CompletableFuture.runAsync(() -> send405MethodNotAllowed(exchange));
        }
    }


    private void sendFileResponse(HttpExchange exchange, File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            exchange.sendResponseHeaders(200, fileBytes.length);
            exchange.getResponseBody().write(fileBytes);
            exchange.getResponseBody().close();
            System.out.println("sendFileResponse method");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            CompletableFuture.runAsync(() -> send500Error(exchange));
        }
    }


    private void send404Response(HttpExchange exchange) {
        try {
            String response = "404 Not Found";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            System.out.println("send404Response method");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void send405MethodNotAllowed(HttpExchange exchange) {
        try {
            String response = "405 Method Not Allowed";
            exchange.sendResponseHeaders(405, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            System.out.println("send405MethodNotAllowed method");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void send500Error(HttpExchange exchange) {
        try {
            String response = "500 Internal Server Error";
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
            exchange.getResponseBody().close();
            System.out.println("send500Error method");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }


    private void logRequest(HttpExchange exchange) {
        String clientIp = exchange.getRemoteAddress().getAddress().getHostAddress();
        String requestedPath = exchange.getRequestURI().getPath();
        int responseCode = exchange.getResponseCode();

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss Z");
        String date = formatter.format(new Date());

        String logEntry = date + " - " + clientIp + " - " + requestedPath + " - " + responseCode;


        try (BufferedWriter writer = new BufferedWriter(new FileWriter("server.log", true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
