package eus.ehu.javafxstatus;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MyJavaFXApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // JavaFX application setup
        Label label = new Label("Hello from JavaFX!");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Add to system tray
        if (SystemTray.isSupported()) {
            addAppToTray(primaryStage);
        } else {
            System.out.println("System tray not supported");
        }
    }

    private void addAppToTray(Stage stage) {
        try {
            // Ensure AWT toolkit is initialized
            java.awt.Toolkit.getDefaultToolkit();

            // Get the system tray
            SystemTray tray = SystemTray.getSystemTray();

            // Load an icon for the tray (use a proper image path)
            Image image = Toolkit.getDefaultToolkit().createImage("/opt/javafxstatus/icon.png");

            // Create a tray icon
            TrayIcon trayIcon = new TrayIcon(image, "JavaFX App");

            // Add a click listener to the tray icon
            trayIcon.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Show the JavaFX application window
                    stage.show();
                }
            });

            // Add the tray icon to the system tray
            tray.add(trayIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
