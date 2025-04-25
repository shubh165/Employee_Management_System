package employee.management.system.client;  // Define the package name for client-side code

import javax.swing.*;  // Import Swing library for GUI components
import java.io.IOException;  // Import IOException for handling input/output exceptions

public class Client {
    public static void main(String[] args) {
        // Start the client UI and connect to the server using Swing's Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            try {
                // Attempt to connect to the server using ConnectionManager
                // Parameters: "localhost" as server IP, and port 12345
                boolean connected = ConnectionManager.connect("localhost", 12345);

                if (connected) {
                    // If connection is successful, launch a new thread to display the splash screen
                    new Thread(() -> {
                        try {
                            // Wait for 5 seconds (simulate splash screen delay)
                            Thread.sleep(5000);

                            // After delay, initialize the Splash screen (which will eventually lead to login)
                            new Splash();
                        } catch (InterruptedException e) {
                            e.printStackTrace();  // Handle thread interruption
                        }
                    }).start();
                } else {
                    // If the connection fails, notify via console
                    System.out.println("Failed to connect to server.");
                }
            } catch (Exception e) {
                e.printStackTrace();  // Catch and print any exceptions during the connection attempt
            }
        });
    }
}

