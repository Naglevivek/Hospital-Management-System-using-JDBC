package HospitalManagementSystem;

import java.sql.*;

public class Doctor {
    private Connection connection; // Encapsulation ke liye private rakha hai isko.

    public Doctor(Connection connection) { // Main class se data aayega toh usko lene ke liye constructor banaya hai
        this.connection = connection;
    }

    public void viewDoctors() {
        String query = "SELECT * FROM doctors;";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println("Doctors: ");
            System.out.println("+------------+--------------------+-----------------------+");
            System.out.println("| Doctor ID  | Name               | Specialization        |");
            System.out.println("+------------+--------------------+-----------------------+");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");
                System.out.printf("| %-10d | %-18s | %-20s |%n", id, name, specialization);
                System.out.println("+------------+--------------------+-----------------------+");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean getDoctorByID(int id) {
        String query = "SELECT * FROM doctors WHERE id = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
