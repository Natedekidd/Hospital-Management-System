import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class InsertTreatment {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // Replace with your MySQL password

    public static void main(String[] args) {
        // Sample data for treatment
        int patientID = 1; // Assuming patient ID 1 exists in the Patient table
        int doctorID = 6;  // Assuming doctor ID 1 exists in the Doctor table
        String treatmentDetails = "Took injection";
        LocalDateTime treatmentDate = LocalDateTime.now();

        // Insert treatment record into database
        if (insertTreatment(patientID, doctorID, treatmentDetails, treatmentDate)) {
            System.out.println("Treatment record inserted successfully");
        } else {
            System.out.println("Failed to insert treatment record");
        }
    }

    private static boolean insertTreatment(int patientID, int doctorID, String treatmentDetails, LocalDateTime treatmentDate) {
        String sql = "INSERT INTO Treatment (patientID, doctorID, treatmentDetails, treatmentDate) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientID);
            pstmt.setInt(2, doctorID);
            pstmt.setString(3, treatmentDetails);
            pstmt.setObject(4, treatmentDate);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
