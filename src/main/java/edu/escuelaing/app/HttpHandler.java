package edu.escuelaing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Handles HTTP requests and responses.
 */

public class HttpHandler implements Runnable {

    private final Socket client;

    /**
     * Constructor for HttpHandler.
     * 
     * @param client  The client socket.
     * @param webRoot The root directory for serving files.
     */

    public HttpHandler(Socket client, String webRoot) {
        this.client = client;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    /**
     * Handles the HTTP request and generates a response.
     */

    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                OutputStream out = client.getOutputStream()) {

            String line = in.readLine();
            if (line == null) {
                return;
            }

            String[] parts = line.split(" ");
            String method = parts[0];
            String uri = parts[1];

            String path = uri;
            String query = null;
            int q = uri.indexOf('?');
            if (q >= 0) {
                path = uri.substring(0, q);
                query = uri.substring(q + 1);
            }

            if (path.equals("/")) {
                path = "/index.html";
            }

            Router router = Router.getInstance();
            Route route = router.findRoute(path);

            if (route != null && method.equals("GET")) {
                try {
                    Request request = new Request(method, path, query);
                    Response response = new Response();

                    String responseBody = route.getHandler().handle(request, response);
                    sendResponse(out, response.getStatusCode(), responseBody, response.getContentType());
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    sendError(out, 500, "Internal Server Error");
                    return;
                }
            }

            // Static Files
            StaticFileHandler staticHandler = StaticFileHandler.getInstance();
            try (InputStream staticFile = staticHandler.getStaticFile(path)) {
                if (staticFile != null) {
                    byte[] data = staticFile.readAllBytes();
                    send(out, data, MimeTypes.get(path));
                    return;
                }
            }

            // GET /hello?name=Cristian
            if (path.equals("/hello") && method.equals("GET")) {
                String name = getParam(query, "name");
                String msg = "Hola " + name + " desde GET!";
                send(out, msg, "text/plain");
                return;
            }

            // POST /hellopost?name=Cristian
            if (path.equals("/hellopost") && method.equals("POST")) {
                String name = getParam(query, "name");
                String msg = "Hola " + name + " desde POST!";
                send(out, msg, "text/plain");
                return;
            }

            // GET /api/time
            if (path.equals("/api/time") && method.equals("GET")) {
                String now = java.time.LocalDateTime.now().toString();
                String json = "{\"time\": \"" + now + "\"}";
                send(out, json, "application/json");
                return;
            }

            // Resources
            try (InputStream file = getClass().getResourceAsStream(path)) {
                if (file != null) {
                    byte[] data = file.readAllBytes();
                    send(out, data, MimeTypes.get(path));
                } else {
                    send(out, "<h1>404 Not Found</h1>", "text/html");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts a query parameter from the query string.
     *
     * @param query the query string
     * @param key   the parameter key
     * @return the parameter value, or an empty string if not found
     */

    private String getParam(String query, String key) {
        if (query == null || query.isEmpty()) {
            return "";
        }
        for (String pair : query.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2 && kv[0].equals(key)) {
                return URLDecoder.decode(kv[1], StandardCharsets.UTF_8);
            }
        }
        return "";
    }

    /**
     * Sends HTTP response with a string body.
     *
     * @param out  the output stream
     * @param body the response body
     * @param type the content type
     * @throws IOException if an I/O error occurs
     */

    private void send(OutputStream out, String body, String type) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        send(out, data, type);
    }

    /**
     * Sends HTTP response with a byte array body.
     *
     * @param out  the output stream
     * @param body the response body
     * @param type the content type
     * @throws IOException if an I/O error occurs
     */

    private void send(OutputStream out, byte[] body, String type) throws IOException {
        out.write(("HTTP/1.1 200 OK\r\n").getBytes());
        out.write(("Content-Type: " + type + "\r\n").getBytes());
        out.write(("Content-Length: " + body.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(body);
    }

    /**
     * Sends HTTP response with custom status code.
     *
     * @param out         the output stream
     * @param statusCode  the HTTP status code
     * @param body        the response body
     * @param contentType the content type
     * @throws IOException if an I/O error occurs
     */

    private void sendResponse(OutputStream out, int statusCode, String body, String contentType) throws IOException {
        byte[] data = body.getBytes(StandardCharsets.UTF_8);
        String statusText = getStatusText(statusCode);
        out.write(("HTTP/1.1 " + statusCode + " " + statusText + "\r\n").getBytes());
        out.write(("Content-Type: " + contentType + "\r\n").getBytes());
        out.write(("Content-Length: " + data.length + "\r\n").getBytes());
        out.write("\r\n".getBytes());
        out.write(data);
    }

    /**
     * Sends HTTP error response.
     *
     * @param out        the output stream
     * @param statusCode the HTTP status code
     * @param message    the error message
     * @throws IOException if an I/O error occurs
     */

    private void sendError(OutputStream out, int statusCode, String message) throws IOException {
        String body = "<h1>" + statusCode + " " + message + "</h1>";
        sendResponse(out, statusCode, body, "text/html");
    }

    /**
     * Gets status text for HTTP status codes.
     *
     * @param statusCode the HTTP status code
     * @return the status text
     */

    private String getStatusText(int statusCode) {
        return switch (statusCode) {
            case 200 -> "OK";
            case 400 -> "Bad Request";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Unknown";
        };
    }
}
