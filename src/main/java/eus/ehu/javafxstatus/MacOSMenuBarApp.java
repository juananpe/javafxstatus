package eus.ehu.javafxstatus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class MacOSMenuBarApp {

    public static void main(String[] args) throws IOException {
        // Start the HTTP server
        startHttpServer();

        // Ensure that the system supports tray icons
        if (!SystemTray.isSupported()) {
            System.out.println("System tray not supported");
            return;
        }

        // Load a blue square icon (or create one programmatically)
        Image image = Toolkit.getDefaultToolkit().createImage(MacOSMenuBarApp.class.getResource("/icon.png"));

        // Create a popup menu
        PopupMenu popup = new PopupMenu();

        // Add a "Hello world" menu item
        MenuItem helloItem = new MenuItem("Hello world");
        helloItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hello world clicked");
            }
        });
        popup.add(helloItem);

        // Create the tray icon with the blue square
        TrayIcon trayIcon = new TrayIcon(image, "JavaFX App", popup);

        // Set the icon to be auto-resizable
        trayIcon.setImageAutoSize(true);

        // Add the tray icon to the system tray
        try {
            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    // Start the HTTP server listening on port 9999
    public static void startHttpServer() throws IOException {
        // Create an HttpServer instance on port 9999
        HttpServer server = HttpServer.create(new InetSocketAddress(9999), 0);

        // Create a context to handle POST requests to "/"
        server.createContext("/", new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                // Handle CORS preflight request (OPTIONS)
                if ("OPTIONS".equals(exchange.getRequestMethod())) {
                    handleOptionsRequest(exchange);
                }

                // Only handle POST requests
                if ("POST".equals(exchange.getRequestMethod())) {
                    // Read the request body (the JSON payload)
                    String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);

                    // Log the received JSON data (you could parse it with a library like Jackson)
                    System.out.println("Received POST data: " + requestBody);

                    // Simulating JSON parsing (you can use a proper JSON parser)
                    if (requestBody.contains("\"timeout\":0")) {
                        // If timeout is 0, show an alert notification
                        showNotification("Alert", "Timeout value is 0!", true);
                    }

                    // Send the CORS headers and a response back to the client
                    String response = "JSON received: " + requestBody;
                    exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                    exchange.sendResponseHeaders(200, response.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        // Start the server
        server.setExecutor(null); // Creates a default executor
        server.start();
        System.out.println("HTTP server started on port 9999");
    }

    // Handle the preflight OPTIONS request for CORS
    private static void handleOptionsRequest(HttpExchange exchange) throws IOException {
        // Add CORS headers to the response
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        // Send a 204 No Content response (no body needed for OPTIONS)
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    // Show a native macOS notification using AppleScript
    public static void showNotification(String title, String message, boolean isError) {
        try {
            // AppleScript command for native notification
            String color = isError ? "red" : "green";
            String script = "display notification \"" + message + "\" with title \"" + title + "\" subtitle \"Alert\" sound name \"Basso\"";

            // Execute the AppleScript command
            ProcessBuilder processBuilder = new ProcessBuilder("osascript", "-e", script);
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
