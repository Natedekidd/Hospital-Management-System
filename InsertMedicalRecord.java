import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class InsertMedicalRecord {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password


    public static void main(String[] args) {
        // Sample data for medical record
        int patientID = 1; // Assuming patient ID 1 exists in the Patient table
        int doctorID = 6;  // Assuming doctor ID 1 exists in the Doctor table
        String diagnosis = "Headache";
        String treatment = "Rest and painkillers";
        LocalDateTime dateOfRecord = LocalDateTime.now();

        // Insert medical record into database
        if (insertMedicalRecord(patientID, doctorID, diagnosis, treatment, dateOfRecord)) {
            System.out.println("Medical record inserted successfully");
        } else {
            System.out.println("Failed to insert medical record");
        }
    }

    private static boolean insertMedicalRecord(int patientID, int doctorID, String diagnosis, String treatment, LocalDateTime dateOfRecord) {
        String sql = "INSERT INTO MedicalRecord (patientID, doctorID, diagnosis, treatment, dateOfRecord) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientID);
            pstmt.setInt(2, doctorID);
            pstmt.setString(3, diagnosis);
            pstmt.setString(4, treatment);
            pstmt.setObject(5, dateOfRecord);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}

