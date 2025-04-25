package employee.management.system.client; // Declares the package for client-side components

import javax.swing.*;            // Imports Swing classes for GUI elements
import java.awt.*;               // Imports AWT classes for image and layout handling
import java.io.BufferedReader;   // Imports BufferedReader for input stream from server
import java.io.PrintWriter;      // Imports PrintWriter for output stream to server
import java.net.Socket;          // Imports Socket for client-server connection

// Splash class that extends JFrame to create a splash screen window
public class Splash extends JFrame {

    // Constructor for the Splash screen
    public Splash() {
        // Get the socket and I/O streams from the ConnectionManager (used later in the app)
        Socket clientSocket = ConnectionManager.getClientSocket(); // Establish socket connection to the server
        PrintWriter out = ConnectionManager.getOutputStream();     // Get output stream to send data to the server
        BufferedReader in = ConnectionManager.getInputStream();    // Get input stream to receive data from the server

        // Load splash screen image using ImageIcon from resource folder "icons/front.gif"
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/front.gif"));

        // Scale the image to fit the splash screen window
        Image i2 = i1.getImage().getScaledInstance(1170, 650, Image.SCALE_DEFAULT);

        // Create a new ImageIcon from the scaled image
        ImageIcon i3 = new ImageIcon(i2);

        // Create a JLabel to hold the splash image
        JLabel image = new JLabel(i3);
        image.setBounds(0, 0, 1170, 650); // Set the position and size of the image
        add(image);                      // Add the image label to the JFrame

        // Set the size of the splash screen window
        setSize(1170, 650);

        // Set the window location on the screen
        setLocation(200, 50);

        // Use absolute positioning (no layout manager)
        setLayout(null);

        // Make the splash screen visible
        setVisible(true);

        // Pause for 5 seconds to display the splash screen
        try {
            Thread.sleep(5000);   // Pause for 5 seconds (5000 ms)
            setVisible(false);   // Hide splash screen
            new Login();         // Launch the Login window after splash
        } catch (Exception e) {
            e.printStackTrace(); // Print any exceptions that occur
        }
    }

    // Main method to run the Splash screen application
    public static void main(String[] args) {
        new Splash();  // Create and display the Splash screen
    }
}


