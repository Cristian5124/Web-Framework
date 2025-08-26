package edu.escuelaing.app;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * web framework with REST support.
 */

public class WebFramework {

    /**
     * Registers a GET route.
     * 
     * @param path    the route path
     * @param handler the route handler
     */

    public static void get(String path, RouteHandler handler) {
        Router.getInstance().addRoute(path, handler);
    }

    /**
     * Sets the static files directory.
     * 
     * @param path the static files directory path
     */

    public static void staticfiles(String path) {
        StaticFileHandler.getInstance().setStaticFilesPath(path);
        System.out.println("Static files configured for: " + path);
    }

    /**
     * Main entry point for the web framework.
     * 
     * @param args command-line arguments
     * @throws Exception if an error occurs
     */

    public static void main(String[] args) throws Exception {
        // Static files location
        staticfiles("/webroot");

        get("/hello", (req, resp) -> {
            String name = req.getValues("name");
            if (name.isEmpty())
                name = "World";
            return "Hello " + name + "!";
        });

        get("/pi", (req, resp) -> String.valueOf(Math.PI));

        get("/time", (req, resp) -> {
            resp.setContentType("application/json");
            String now = java.time.LocalDateTime.now().toString();
            return "{\"time\": \"" + now + "\", \"message\": \"Current server time\"}";
        });

        get("/greet", (req, resp) -> {
            String name = req.getValues("name");
            String lang = req.getValues("lang");
            if (name.isEmpty())
                name = "Friend";

            String greeting;
            greeting = switch (lang.toLowerCase()) {
                case "es" -> "Hola";
                case "fr" -> "Bonjour";
                case "de" -> "Hallo";
                default -> "Hello";
            };
            return greeting + " " + name + "! Welcome to the web framework.";
        });

        get("/calc", (req, resp) -> {
            try {
                String op = req.getValues("op");
                double a = Double.parseDouble(req.getValues("a"));
                double b = Double.parseDouble(req.getValues("b"));
                double result;

                switch (op) {
                    case "add" -> result = a + b;
                    case "sub" -> result = a - b;
                    case "mul" -> result = a * b;
                    case "div" -> {
                        if (b == 0) {
                            resp.setStatusCode(400);
                            return "Error: Division by zero";
                        }
                        result = a / b;
                    }
                    default -> {
                        resp.setStatusCode(400);
                        return "Error: Unknown operation";
                    }
                }

                resp.setContentType("application/json");
                return "{\"result\": " + result + ", \"operation\": \"" + op +
                        "\", \"operands\": [" + a + ", " + b + "]}";
            } catch (NumberFormatException e) {
                resp.setStatusCode(400);
                return "Error: Invalid number format";
            }
        });

        int port = 8080;
        try (ServerSocket server = new ServerSocket(port)) {
            System.out.println("Web Framework Server running on http://localhost:" + port);
            while (true) {
                Socket client = server.accept();
                new Thread(new HttpHandler(client, "")).start();
            }
        }
    }
}
