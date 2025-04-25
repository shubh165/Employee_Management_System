package employee.management.system.server;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Server {

    // Global connection to MySQL database (shared by all threads)
    private static Connection globalConnection;

    // Lock to ensure thread safety when establishing database connection
    private static final Lock lock = new ReentrantLock();

    public static void main(String[] args) {
        try {
            // Establish global database connection when server starts
            establishConnection();
            System.out.println("Server started... Waiting for clients...");

            // Create a server socket listening on port 12345
            try (ServerSocket serverSocket = new ServerSocket(12345)) {
                // Continuously accept client connections
                while (true) {
                    Socket clientSocket = serverSocket.accept(); // Wait for a client
                    new Thread(() -> handleClient(clientSocket)).start(); // Handle each client in a new thread
                }
            }

        } catch (IOException | SQLException e) {
            e.printStackTrace(); // Print exception stack trace
        }
    }

    // Establish the global database connection (executed once)
    private static void establishConnection() throws SQLException {
        lock.lock();  // Acquire lock to ensure only one thread executes this block
        try {
            if (globalConnection == null || globalConnection.isClosed()) {
                globalConnection = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/employeemanagement", "root", "9909");
                System.out.println("Database connection established.");
            }
        } finally {
            lock.unlock(); // Always release lock after use
        }
    }

    // Handle communication with a single connected client
    private static void handleClient(Socket socket) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); // Read client input
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true) // Send responses to client
        ) {
            String request;

            // Keep reading client requests until the connection is closed
            while ((request = reader.readLine()) != null) {
                System.out.println("Received: " + request); // Log the request

                String[] parts = request.split(";"); // Split input by semicolon
                if (parts.length < 1) {
                    writer.println("Error: Invalid request format.");
                    continue;
                }

                String command = parts[0].trim(); // Extract command (e.g., ADD, DELETE, etc.)

                // Execute command based on input
                switch (command) {
                    case "LOGIN":
                        handleLogin(parts, writer);
                        break;
                    case "ADD":
                        handleAddEmployee(parts, writer);
                        break;
                    case "DELETE":
                        handleDeleteEmployee(parts, writer);
                        break;
                    case "FETCH":
                        handleFetchEmployee(parts, writer);
                        break;
                    case "FETCH_IDS":
                        handleFetchAllEmployeeIds(writer);
                        break;
                    case "UPDATE":
                        handleUpdateEmployee(parts, writer);
                        break;
                    case "REMOVE":
                        handleRemoveEmployee(parts, writer);
                        break;
                    default:
                        writer.println("Error: Invalid command received.");
                }
            }

            System.out.println("Client disconnected.");

        } catch (IOException e) {
            e.printStackTrace();
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("Error: Connection issue occurred.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
                writer.println("Error: An unexpected error occurred.");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                socket.close(); // Always close the socket
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Handles user login
    private static void handleLogin(String[] data, PrintWriter writer) {
        if (data.length < 3) {
            writer.println("Invalid login request.");
            return;
        }

        String username = data[1];
        String password = data[2];

        try {
            String query = "SELECT * FROM login WHERE username = ? AND password = ?";
            PreparedStatement ps = globalConnection.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                writer.println("SUCCESS"); // Valid credentials
            } else {
                writer.println("FAILURE"); // Invalid credentials
            }
        } catch (SQLException e) {
            writer.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handles adding a new employee
    private static void handleAddEmployee(String[] data, PrintWriter writer) {
        if (data.length != 12) {
            writer.println("Insufficient data to add employee.");
            return;
        }

        try {
            String query = "INSERT INTO employee (name, fname, dob, salary, address, phone, email, education, designation, addhar, empid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = globalConnection.prepareStatement(query);

            for (int i = 1; i <= 11; i++) {
                ps.setString(i, data[i]); // Set parameters in prepared statement
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                writer.println("Employee added successfully.");
            } else {
                writer.println("Failed to add employee.");
            }
        } catch (SQLException e) {
            writer.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handles deleting an employee
    private static void handleDeleteEmployee(String[] data, PrintWriter writer) {
        if (data.length < 2) {
            writer.println("Insufficient data to delete employee.");
            return;
        }

        String empId = data[1];

        try {
            String query = "DELETE FROM employee WHERE empid = ?";
            PreparedStatement ps = globalConnection.prepareStatement(query);
            ps.setString(1, empId);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                writer.println("Employee deleted successfully.");
            } else {
                writer.println("Employee with ID " + empId + " not found.");
            }
        } catch (SQLException e) {
            writer.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Handles fetching all employee IDs
    private static void handleFetchAllEmployeeIds(PrintWriter writer) {
        try {
            Statement stmt = globalConnection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM employee");

            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append(rs.getString("empId")).append(";");
            }
            writer.println(result); // Send all employee IDs separated by ;
            rs.close();
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("Error: Failed to fetch employee IDs.");
        }
    }

    // Handles fetching full employee details
    private static void handleFetchEmployee(String[] parts, PrintWriter writer) {
        if (parts.length < 2) {
            writer.println("Error: Missing employee ID for FETCH.");
            return;
        }

        String empId = parts[1];
        try {
            PreparedStatement ps = globalConnection.prepareStatement("SELECT * FROM employee WHERE empId = ?");
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // Concatenate all employee data and send to client
                String data = String.join(";", "DATA",
                        rs.getString("name"),
                        rs.getString("fname"),
                        rs.getString("dob"),
                        rs.getString("salary"),
                        rs.getString("address"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("education"),
                        rs.getString("designation"),
                        rs.getString("addhar"),
                        rs.getString("empId")
                );
                writer.println(data);
            } else {
                writer.println("Error: No employee found.");
            }

            rs.close();
            ps.close();
        } catch (Exception e) {
            e.printStackTrace();
            writer.println("Error: Failed to fetch employee data.");
        }
    }

    // Handles updating an employee's details
    private static void handleUpdateEmployee(String[] parts, PrintWriter writer) {
        if (parts.length != 9) {
            writer.println("Error: Insufficient data to update employee.");
            return;
        }

        String empId = parts[1];
        String fname = parts[2];
        String salary = parts[3];
        String address = parts[4];
        String phone = parts[5];
        String email = parts[6];
        String education = parts[7];
        String designation = parts[8];

        try {
            String updateQuery = "UPDATE employee SET fname = ?, salary = ?, address = ?, phone = ?, email = ?, education = ?, designation = ? WHERE empId = ?";
            PreparedStatement stmt = globalConnection.prepareStatement(updateQuery);

            stmt.setString(1, fname);
            stmt.setString(2, salary);
            stmt.setString(3, address);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, education);
            stmt.setString(7, designation);
            stmt.setString(8, empId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                writer.println("Success: Employee details updated.");
            } else {
                writer.println("Error: No employee found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error: Unable to update employee details.");
        }
    }

    // Handles removing an employee (same as delete, but different message format)
    private static void handleRemoveEmployee(String[] parts, PrintWriter writer) {
        if (parts.length != 2) {
            writer.println("Error: Insufficient data to remove employee.");
            return;
        }

        String empId = parts[1];

        try {
            String query = "DELETE FROM employee WHERE empId = ?";
            PreparedStatement stmt = globalConnection.prepareStatement(query);
            stmt.setString(1, empId);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                writer.println("Success: Employee with ID " + empId + " deleted successfully.");
            } else {
                writer.println("Error: No employee found with the given ID.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            writer.println("Error: Unable to delete employee.");
        }
    }
}


