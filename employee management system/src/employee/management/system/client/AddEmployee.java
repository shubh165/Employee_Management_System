package employee.management.system.client; // Package declaration

// Importing necessary libraries
import com.toedter.calendar.JDateChooser; // For date picker component

import javax.swing.*; // For GUI components
import java.awt.*; // For color and layout
import java.awt.event.ActionEvent; // For button event handling
import java.awt.event.ActionListener;
import java.io.*; // For I/O streams
import java.net.Socket; // For socket communication
import java.util.Random; // To generate random employee ID

public class AddEmployee extends JFrame implements ActionListener {
    // Randomly generate an employee ID
    Random ran = new Random();
    int number = ran.nextInt(999999); // Random 6-digit number for employee ID

    // Declare input fields
    JTextField tname, tfname, taddress, tphone, taadhar, temail, tsalary, tdesignation;
    JLabel tempid;
    JDateChooser tdob; // For date of birth

    JButton add, back; // Buttons

    JComboBox<String> Boxeducation; // Dropdown for education

    // Socket and I/O streams for client-server communication
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    // Constructor: sets up GUI and initializes socket/streams
    public AddEmployee() {
        // Get socket and streams from ConnectionManager
        this.clientSocket = ConnectionManager.getClientSocket();
        this.out = ConnectionManager.getOutputStream();
        this.in = ConnectionManager.getInputStream();

        // Set background color of the frame
        getContentPane().setBackground(new Color(163, 255, 188));

        // Heading label
        JLabel heading = new JLabel("Add Employee Detail");
        heading.setBounds(320, 30, 500, 50);
        heading.setFont(new Font("serif", Font.BOLD, 25));
        add(heading);

        // Label and input for name
        JLabel name = new JLabel("Name");
        name.setBounds(50,150,150,30);
        name.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(name);

        tname = new JTextField();
        tname.setBounds(200,150,150,30);
        tname.setBackground(new Color(177,252,197));
        add(tname);

        // Father's name
        JLabel fname = new JLabel("Father's Name");
        fname.setBounds(400,150,150,30);
        fname.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(fname);

        tfname = new JTextField();
        tfname.setBounds(600,150,150,30);
        tfname.setBackground(new Color(177,252,197));
        add(tfname);

        // Date of birth
        JLabel dob = new JLabel("Date Of Birth");
        dob.setBounds(50,200,150,30);
        dob.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(dob);

        tdob = new JDateChooser(); // Calendar input
        tdob.setBounds(200,200,150,30);
        tdob.setBackground(new Color(177,252,197));
        add(tdob);

        // Salary
        JLabel salary = new JLabel("Salary");
        salary.setBounds(400,200,150,30);
        salary.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(salary);

        tsalary = new JTextField();
        tsalary.setBounds(600,200,150,30);
        tsalary.setBackground(new Color(177,252,197));
        add(tsalary);

        // Address
        JLabel address = new JLabel("Address");
        address.setBounds(50,250,150,30);
        address.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(address);

        taddress= new JTextField();
        taddress.setBounds(200,250,150,30);
        taddress.setBackground(new Color(177,252,197));
        add(taddress);

        // Phone number
        JLabel phone = new JLabel("Phone");
        phone.setBounds(400,250,150,30);
        phone.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(phone);

        tphone= new JTextField();
        tphone.setBounds(600,250,150,30);
        tphone.setBackground(new Color(177,252,197));
        add(tphone);

        // Email
        JLabel email = new JLabel("Email");
        email.setBounds(50,300,150,30);
        email.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(email);

        temail= new JTextField();
        temail.setBounds(200,300,150,30);
        temail.setBackground(new Color(177,252,197));
        add(temail);

        // Education dropdown
        JLabel education = new JLabel("Higest Education");
        education.setBounds(400,300,150,30);
        education.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(education);

        String items[] = {"BBA","B.Tech","BCA", "BA", "BSC", "B.COM", "MBA", "MCA", "MA", "MTech", "MSC", "PHD"};
        Boxeducation = new JComboBox(items);
        Boxeducation.setBackground(new Color(177,252,197));
        Boxeducation.setBounds(600,300,150,30);
        add(Boxeducation);

        // Aadhar
        JLabel aadhar = new JLabel("Aadhar Number");
        aadhar.setBounds(400,350,150,30);
        aadhar.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(aadhar);

        taadhar= new JTextField();
        taadhar.setBounds(600,350,150,30);
        taadhar.setBackground(new Color(177,252,197));
        add(taadhar);

        // Employee ID
        JLabel empid = new JLabel("Employee ID");
        empid.setBounds(50,400,150,30);
        empid.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(empid);

        tempid= new JLabel(""+number); // Auto-generated employee ID
        tempid.setBounds(200,400,150,30);
        tempid.setFont(new Font("SAN_SARIF", Font.BOLD,20));
        tempid.setForeground(Color.RED); // Display ID in red
        add(tempid);

        // Designation
        JLabel designation = new JLabel("Designation");
        designation.setBounds(50,350,150,30);
        designation.setFont(new Font("SAN_SERIF", Font.BOLD,20));
        add(designation);

        tdesignation= new JTextField();
        tdesignation.setBounds(200,350,150,30);
        tdesignation.setBackground(new Color(177,252,197));
        add(tdesignation);

        // Add button
        add = new JButton("ADD");
        add.setBounds(450,550,150,40);
        add.setBackground(Color.black);
        add.setForeground(Color.WHITE);
        add.addActionListener(this); // Attach listener
        add(add);

        // Back button
        back = new JButton("BACK");
        back.setBounds(250,550,150,40);
        back.setBackground(Color.black);
        back.setForeground(Color.WHITE);
        back.addActionListener(this);
        add(back);

        // Final frame setup
        setSize(900,700); // Window size
        setLayout(null); // Absolute positioning
        setLocation(300,50); // Screen position
        setVisible(true); // Show the frame
    }

    // Handle button click events
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == add) { // If ADD is clicked
            // Get all values from input fields
            String name = tname.getText();
            String fname = tfname.getText();
            String dob = ((JTextField) tdob.getDateEditor().getUiComponent()).getText();
            String salary = tsalary.getText();
            String address = taddress.getText();
            String aadhar = taadhar.getText();
            String phone = tphone.getText();
            String email = temail.getText();
            String education = (String) Boxeducation.getSelectedItem();
            String designation = tdesignation.getText();
            String empID = tempid.getText();

            // Format data to be sent to server
            String requestData = "ADD;" + name + ";" + fname + ";" + dob + ";" + salary + ";" + address + ";" + phone + ";" + email + ";" + education + ";" + designation + ";" + aadhar + ";" + empID;
            sendDataToServer(requestData); // Send it
        } else {
            // If BACK is clicked, go to main screen
            setVisible(false);
            new Main_class();
        }
    }

    // Method to send data to the server using a new thread
    private void sendDataToServer(String requestData) {
        new Thread(() -> {
            try {
                out.println(requestData); // Send data to server
                String serverResponse = in.readLine(); // Wait for server response

                // Show the server's response in a dialog
                JOptionPane.showMessageDialog(null, serverResponse);

                if ("Employee added successfully.".equals(serverResponse)) {
                    setVisible(false);
                    new Main_class(); // Redirect to main screen
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print error if communication fails
            }
        }).start();
    }

    public static void main(String[] args) {
        // Not used here since constructor is called from elsewhere
    }
}


