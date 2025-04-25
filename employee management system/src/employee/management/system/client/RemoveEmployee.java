package employee.management.system.client;

// Import required packages
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class RemoveEmployee extends JFrame implements ActionListener {
    Choice choiceEMPID;                 // Drop-down list for employee IDs
    JButton delete, back;               // Buttons for deleting and going back

    // Network communication variables
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor
    RemoveEmployee() {
        // Initialize client socket and input/output streams from ConnectionManager
        this.clientSocket = ConnectionManager.getClientSocket();
        this.out = ConnectionManager.getOutputStream();
        this.in = ConnectionManager.getInputStream();

        // Label for Employee ID
        JLabel label = new JLabel("Employee ID");
        label.setBounds(50, 50, 100, 30);
        label.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(label);

        // Dropdown to show employee IDs
        choiceEMPID = new Choice();
        choiceEMPID.setBounds(200, 50, 150, 30);
        add(choiceEMPID);

        // Request to fetch all employee IDs from server
        out.println("FETCH_IDS");
        try {
            String idsLine = in.readLine(); // Read IDs from server
            if (idsLine != null && !idsLine.startsWith("Error")) {
                String[] ids = idsLine.split(";"); // Split by semicolon
                for (String id : ids) {
                    choiceEMPID.add(id.trim()); // Add each ID to the dropdown
                }
            } else {
                JOptionPane.showMessageDialog(null, "Failed to load employee IDs.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Labels for employee details (Name, Phone, Email)
        JLabel labelName = new JLabel("Name");
        labelName.setBounds(50, 100, 100, 30);
        labelName.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(labelName);

        JLabel textName = new JLabel();
        textName.setBounds(200, 100, 100, 30);
        add(textName);

        JLabel labelPhone = new JLabel("Phone");
        labelPhone.setBounds(50, 150, 100, 30);
        labelPhone.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(labelPhone);

        JLabel textPhone = new JLabel();
        textPhone.setBounds(200, 150, 100, 30);
        add(textPhone);

        JLabel labelemail = new JLabel("Email");
        labelemail.setBounds(50, 200, 100, 30);
        labelemail.setFont(new Font("Tahoma", Font.BOLD, 15));
        add(labelemail);

        JLabel textEmail = new JLabel();
        textEmail.setBounds(200, 200, 100, 30);
        add(textEmail);

        // Initial fetch and display of employee details for selected ID
        try {
            String number = choiceEMPID.getSelectedItem();        // Selected employee ID
            String request = "FETCH;" + number;                   // Request to fetch details
            out.println(request);
            String response = in.readLine();                        // Read server response
            if (response != null && response.startsWith("DATA")) {
                String[] parts = response.split(";");
                if (parts.length == 12) {
                    textName.setText(parts[1]);                   // Set Name
                    textPhone.setText(parts[6]);                  // Set Phone
                    textEmail.setText(parts[7]);                  // Set Email
                }
            }
        } catch (Exception ef) {
            ef.printStackTrace();
        }

        // Update name, phone, and email when a new ID is selected
        choiceEMPID.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                try {
                    String number = choiceEMPID.getSelectedItem();
                    String request = "FETCH;" + number;
                    out.println(request);
                    String response = in.readLine();
                    if (response != null && response.startsWith("DATA")) {
                        String[] parts = response.split(";");
                        if (parts.length == 12) {
                            textName.setText(parts[1]);
                            textPhone.setText(parts[6]);
                            textEmail.setText(parts[7]);
                        }
                    }
                } catch (Exception ef) {
                    ef.printStackTrace();
                }
            }
        });

        // Delete button setup
        delete = new JButton("Delete");
        delete.setBounds(80, 300, 100, 30);
        delete.setBackground(Color.black);
        delete.setForeground(Color.WHITE);
        delete.addActionListener(this); // Register click listener
        add(delete);

        // Back button setup
        back = new JButton("Back");
        back.setBounds(220, 300, 100, 30);
        back.setBackground(Color.black);
        back.setForeground(Color.WHITE);
        back.addActionListener(this); // Register click listener
        add(back);

        // Decorative icon (delete.png)
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("icons/delete.png"));
        Image i2 = i1.getImage().getScaledInstance(200, 200, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel img = new JLabel(i3);
        img.setBounds(700, 80, 200, 200);
        add(img);

        // Background image
        ImageIcon i11 = new ImageIcon(ClassLoader.getSystemResource("icons/rback.png"));
        Image i22 = i11.getImage().getScaledInstance(1120, 630, Image.SCALE_DEFAULT);
        ImageIcon i33 = new ImageIcon(i22);
        JLabel image = new JLabel(i33);
        image.setBounds(0, 0, 1120, 630);
        add(image);

        // JFrame properties
        setSize(1000, 400);
        setLocation(300, 150);
        setLayout(null);
        setVisible(true);
    }

    // Handle button clicks
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == delete) {
            try {
                // Send delete request to server
                String removeRequest = "REMOVE;" + choiceEMPID.getSelectedItem();
                out.println(removeRequest);
                String response = in.readLine(); // Read server's response

                JOptionPane.showMessageDialog(null, response); // Show result message

                if (response.startsWith("Success")) {
                    setVisible(false);             // Close current window
                    new Main_class();              // Return to main window
                }
            } catch (Exception E) {
                E.printStackTrace();
            }
        } else {
            setVisible(false);                     // Close window if back is clicked
        }
    }

    // Main method to test the window standalone
    public static void main(String[] args) {
        new RemoveEmployee();
    }
}

