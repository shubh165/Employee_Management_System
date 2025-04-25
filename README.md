# Employee Management System

A Java-based client-server desktop application for efficiently managing employee records with real-time communication and multithreaded operations. Built using **Java Swing** for GUI, **JDBC** for database interaction, and **Sockets with multithreading** for network-based functionality.

# 🔧 Features

- **Add Employee**: Register new employees with details like name, position, salary, etc.

- **View Employees**: Display a structured list of all registered employees.

- **Update Employee**: Modify existing employee records.

- **Delete Employee**: Remove an employee entry from the database.
 
- **Search Employee**: Locate employee records using ID or name.

- **Real-Time Server Communication**: All operations are handled through a multithreaded server for real-time response.

- **Multithreaded Architecture**: Each client interaction is handled via separate threads for better concurrency.
 
- **Secure Database Integration**: Stores employee records using **MySQL** via **JDBC**.

- **Modern GUI**: Intuitive Java Swing-based interface for smooth navigation.


# 💻 Technologies Used

| Component            | Technology         |
|----------------------|--------------------|
| Programming Language | Java               |
| GUI Framework        | Swing              |
| Database             | MySQL              |
| Networking           | Java Sockets       |
| Multithreading       | Java Threads       |
| JDBC Driver          | MySQL Connector/J  |

# 🚀 Installation & Setup

## ✅ Prerequisites

- Java JDK 8 or later installed
- MySQL Server with a user account and a database named `employeemanagement`
- MySQL JDBC Driver (Connector/J)

## 📦 Setup Steps

### 1. Clone the repository

bash
git clone https://github.com/shubh165/Employee-Management.git

### 2. Open in your preferred Java IDE

IntelliJ IDEA / Eclipse / NetBeans

### 3. Import the Driver

Add the .jar file (e.g., mysql-connector-java-8.x.x.jar) to your project libraries.
Add all the .jar file from External_Library that present inside employee management system Folder.

### 4. Configure the database connection

Open server.java and update:

String url = "jdbc:mysql://localhost:3306/employeemanagement";
String user = "root";
String password = "yourpassword"; // Replace with your MySQL password

After this write sql code in Mysql Workbench.
sql code present in DataBse.sql file inside employee management system Folder.

### 5. Start the Server

Run Server.java to launch the multithreaded server.

### 6. Run the Client

Run Client.java to start the GUI-based application on the client side.

## 🧑‍💼 Usage Guide
- Start the server by running Server.java.

- Then launch the client by running Client.java.

- Use the GUI to:

    Add, update, delete, or search employees.

- All operations are processed via server with real-time communication.

## 👥 Developed By

Shubhendra Tiwari (524110049)

Aman Farkya (524110059)

Ankur Raj (524410026)

Sanjog Mucchal (524410029)

# 🤝 Contributing
We welcome contributions from the community!

1. Fork the repository

2. Create a new branch

    bash
    git checkout -b feature-branch

3. Commit your changes

    bash
    git commit -m "Add new feature"

4. Push to your branch

    bash
    git push origin feature-branch

5. Open a Pull Request

# 📌 Notes
- Ensure the server is running before launching the client.

- The application supports multiple clients simultaneously using multithreading.
