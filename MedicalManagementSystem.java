package medical;
import java.sql.*;
import java.util.Scanner;

public class MedicalManagementSystem {
    static final String JDBC_URL = "jdbc:mysql://localhost/medical_db";
    static final String JDBC_USER = "root";
    static final String JDBC_PASSWORD = "root";

    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD)) {
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
                System.out.println("1. Add patient");
                System.out.println("2. Update patient");
                System.out.println("3. Delete patient");
                System.out.println("4. View patients");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                switch (choice) {
                    case 1:
                        addPatient(connection, scanner);
                        break;
                    case 2:
                        updatePatient(connection, scanner);
                        break;
                    case 3:
                        deletePatient(connection, scanner);
                        break;
                    case 4:
                        viewPatients(connection);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            } while (choice != 5);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void addPatient(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter patient name: ");
        String name = scanner.next();
        System.out.print("Enter patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter patient gender: ");
        String gender = scanner.next();
        System.out.print("Enter patient address: ");
        String address = scanner.next();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "INSERT INTO patients (name, age, gender, address) VALUES (?, ?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, address);
            preparedStatement.executeUpdate();
            System.out.println("Patient added successfully.");
        }
    }

    private static void updatePatient(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter patient ID to update: ");
        int id = scanner.nextInt();
        System.out.print("Enter new patient name: ");
        String name = scanner.next();
        System.out.print("Enter new patient age: ");
        int age = scanner.nextInt();
        System.out.print("Enter new patient gender: ");
        String gender = scanner.next();
        System.out.print("Enter new patient address: ");
        String address = scanner.next();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "UPDATE patients SET name=?, age=?, gender=?, address=? WHERE id=?")) {
            preparedStatement.setString(1, name);
            preparedStatement.setInt(2, age);
            preparedStatement.setString(3, gender);
            preparedStatement.setString(4, address);
            preparedStatement.setInt(5, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient updated successfully.");
            } else {
                System.out.println("Patient not found.");
            }
        }
    }

    private static void deletePatient(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter patient ID to delete: ");
        int id = scanner.nextInt();

        try (PreparedStatement preparedStatement = connection.prepareStatement(
                "DELETE FROM patients WHERE id=?")) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Patient deleted successfully.");
            } else {
                System.out.println("Patient not found.");
            }
        }
    }

    private static void viewPatients(Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM patients")) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String gender = resultSet.getString("gender");
                String address = resultSet.getString("address");
                System.out.println("ID: " + id + ", Name: " + name + ", Age: " + age + ", Gender: " + gender + ", Address: " + address);
            }
        }
    }
}
