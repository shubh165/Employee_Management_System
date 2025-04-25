package employee.management.system.client; // Declares the package

import net.proteanit.sql.DbUtils; // (Optional) Used for converting ResultSet to TableModel, not used in final code

import javax.swing.*; // For Swing UI components
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*; // For layout and colors
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

// This class creates a GUI for viewing employee details and interacting with server
public class View_Employee extends JFrame implements ActionListener {

    JTable table;                 // Table to display employee info
    Choice choiceEMP;            // Dropdown to select employee ID

    String selectedID;           // Stores selected employee ID from dropdown
    JButton searchbtn, print, update, back; // Action buttons

    private Socket clientSocket;         // Client socket
    private PrintWriter out;             // Output stream to send data to server
    private BufferedReader in;           // Input stream to receive data from server

    public View_Employee() {
        // Initialize socket and streams from ConnectionManager
        this.clientSocket = ConnectionManager.getClientSocket();
        this.out = ConnectionManager.getOutputStream();
        this.in = ConnectionManager.getInputStream();

        // Set background color of the content pane
        getContentPane().setBackground(new Color(255,131,122));

        // Label for search dropdown
        JLabel search = new JLabel("Search by employee id");
        search.setBounds(20,20,150,20);
        add(search);

        // Dropdown to hold employee IDs
        choiceEMP = new Choice();
        choiceEMP.setBounds(180,20,150,20);
        add(choiceEMP);

        // Request server for list of employee IDs
        out.println("FETCH_IDS");
        try {
            String idsLine = in.readLine(); // Read IDs response from server
            if (idsLine != null && !idsLine.startsWith("Error")) {
                String[] ids = idsLine.split(";"); // Split IDs by semicolon
                for (String id : ids) {
                    choiceEMP.add(id.trim()); // Add each ID to the dropdown
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load employee IDs.");
            }
        } catch (Exception e) {
            e.printStackTrace(); // Handle any IO exceptions
        }

        // Table to display employee data
        table = new JTable();
        JScrollPane jp = new JScrollPane(table); // Add scroll pane to table
        jp.setBounds(0,100,900,600);
        add(jp);

        // Button to search selected employee
        searchbtn = new JButton("Search");
        searchbtn.setBounds(20,70,80,20);
        searchbtn.addActionListener(this);
        add(searchbtn);

        // Button to print the table
        print = new JButton("Print");
        print.setBounds(120,70,80,20);
        print.addActionListener(this);
        add(print);

        // Button to update selected employee
        update = new JButton("Update");
        update.setBounds(220,70,80,20);
        update.addActionListener(this);
        add(update);

        // Button to go back to main screen
        back = new JButton("Back");
        back.setBounds(320,70,80,20);
        back.addActionListener(this);
        add(back);

        // Set frame properties
        setSize(900,700);
        setLayout(null);
        setLocation(300,100);
        setVisible(true); // Show the frame
    }

    // Handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == searchbtn) { // When "Search" button is clicked
            selectedID = choiceEMP.getSelectedItem(); // Get selected employee ID
            String request = "FETCH;" + selectedID;   // Create fetch request

            out.println(request); // Send request to server

            try {
                String response = in.readLine(); // Read server response
                if (response != null && response.startsWith("DATA")) {
                    // Parse server data
                    String[] parts = response.split(";");

                    if (parts.length == 12) { // Check expected format
                        // Create row data for table
                        String[][] rowData = {
                                {
                                        parts[1], parts[2], parts[3], parts[4],
                                        parts[5], parts[6], parts[7], parts[8],
                                        parts[9], parts[10], parts[11]
                                }
                        };

                        // Define column names
                        String[] columnNames = {
                                "Name", "Father's Name", "DOB", "Salary", "Address", "Phone",
                                "Email", "Education", "Designation", "Aadhar", "EmpId"
                        };

                        // Set data to table
                        table.setModel(new DefaultTableModel(rowData, columnNames));
                    } else {
                        System.err.println("Response does not have the expected number of columns.");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Employee not found.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } else if (e.getSource() == print) { // Print button action
            try {
                table.print(); // Print table
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else if (e.getSource() == update) { // Update button action
            setVisible(false);
            new UpdateEmployee(selectedID); // Open update form with selected ID
        } else { // Back button action
            setVisible(false);
            new Main_class(); // Go back to main class UI
        }
    }

    // Entry point to test the UI independently
    public static void main(String[] args) {
        new View_Employee();
    }
}


