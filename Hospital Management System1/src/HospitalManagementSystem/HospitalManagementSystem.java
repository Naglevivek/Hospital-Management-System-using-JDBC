package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
   private static final String url = "jdbc:mysql://localhost:3306/hospital";
   private static final String username = "root";
   private static final String password = "Vivek@123";

   public static void main(String[] args) {
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
       } catch (ClassNotFoundException e) {
           e.printStackTrace();
       }

       Scanner scanner = new Scanner(System.in);
       Connection connection = null;

       try {
           connection = DriverManager.getConnection(url, username, password);
           Patient patient = new Patient(connection, scanner);
           Doctor doctor = new Doctor(connection);

           while (true) {
               System.out.println("Hospital Management System.");
               System.out.println("1. Add Patient");
               System.out.println("2. View Patients");
               System.out.println("3. View Doctors");
               System.out.println("4. Book Appointment");
               System.out.println("5. Exit");
               System.out.println("Enter your choice:");
               int choice = scanner.nextInt();

               switch (choice) {
                   case 1:
                       patient.addPatient();
                       break;
                   case 2:
                       patient.viewPatients();
                       break;
                   case 3:
                       doctor.viewDoctors();
                       break;
                   case 4:
                       bookAppointment(patient, doctor, connection, scanner);
                       break;
                   case 5:
                       return;
                   default:
                       System.out.println("Enter valid choice...");
               }
           }
       } catch (SQLException e) {
           e.printStackTrace();
       } finally {
           if (scanner != null) {
               scanner.close();
           }
           if (connection != null) {
               try {
                   connection.close();
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           }
       }
   }

   public static void bookAppointment(Patient patient, Doctor doctor, Connection connection, Scanner scanner) {
       System.out.println("Enter patient id:");
       int patientId = scanner.nextInt();
       System.out.println("Enter doctor id:");
       int doctorId = scanner.nextInt();
       System.out.println("Enter appointment date (YYYY-MM-DD): ");
       String appointmentDate = scanner.next();

       if (patient.getPatientByID(patientId) && doctor.getDoctorByID(doctorId)) {
           if (checkDoctorAvailability(doctorId, appointmentDate, connection)) {
               String query = "INSERT INTO APPOINTMENTS(patient_id, doctor_id, appointment_date) VALUES(?, ?, ?)";
               try {
                   PreparedStatement preparedStatement = connection.prepareStatement(query);
                   preparedStatement.setInt(1, patientId);
                   preparedStatement.setInt(2, doctorId);
                   preparedStatement.setString(3, appointmentDate);
                   int rowsAffected = preparedStatement.executeUpdate();
                   if (rowsAffected > 0) {
                       System.out.println("Appointment booked!");
                   } else {
                       System.out.println("Failed to Book an Appointment!");
                   }
               } catch (SQLException e) {
                   e.printStackTrace();
               }
           } else {
               System.out.println("Doctor not available on this date!!!");
           }
       } else {
           System.out.println("Either doctor or patient doesn't exist!!!");
       }
   }

   public static boolean checkDoctorAvailability(int doctorId, String appointmentDate, Connection connection) {
       String query = "SELECT COUNT(*) FROM APPOINTMENTS WHERE doctor_id=? AND appointment_date=?";
       try {
           PreparedStatement preparedStatement = connection.prepareStatement(query);
           preparedStatement.setInt(1, doctorId);
           preparedStatement.setString(2, appointmentDate);
           ResultSet resultSet = preparedStatement.executeQuery();
           if (resultSet.next()) {
               int count = resultSet.getInt(1);
               return count == 0;
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
       return false;
   }
}
