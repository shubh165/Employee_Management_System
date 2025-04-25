package employee.management.system.client; // Package declaration

import java.io.*;    // Import classes for Input/Output streams
import java.net.*;   // Import classes for networking (Socket, etc.)

// This class manages the client-side socket connection and its input/output streams
public class ConnectionManager {
    private static Socket clientSocket;           // Static socket instance for client-server connection
    private static PrintWriter out;               // Output stream to send data to server
    private static BufferedReader in;             // Input stream to receive data from server

    // Method to connect to the server using host and port
    public static boolean connect(String host, int port) {
        try {
            // Create a socket connection to the specified host and port
            clientSocket = new Socket(host, port);
            System.out.println("Connected to server");

            // Initialize output stream for sending messages to server
            out = new PrintWriter(clientSocket.getOutputStream(), true);

            // Initialize input stream for reading messages from server
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            return true; // Return true if connection successful
        } catch (IOException e) {
            e.printStackTrace(); // Print error if connection fails
            return false;        // Return false if connection failed
        }
    }

    // Getter method to return the output stream
    public static PrintWriter getOutputStream() {
        return out;
    }

    // Getter method to return the input stream
    public static BufferedReader getInputStream() {
        return in;
    }

    // Getter method to return the client socket (useful for managing connection)
    public static Socket getClientSocket() {
        return clientSocket;
    }

    // Method to close the connection and all associated streams
    public static void closeConnection() {
        try {
            if (out != null) out.close();               // Close output stream
            if (in != null) in.close();                 // Close input stream
            if (clientSocket != null) clientSocket.close(); // Close socket
            System.out.println("Connection closed");
        } catch (IOException e) {
            e.printStackTrace(); // Print any exception that occurs while closing
        }
    }
}

