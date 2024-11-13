import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class InsertPrescription {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password


    public static void main(String[] args) {
        // Sample data for prescription
        int patientID = 1; // Assuming patient ID 1 exists in the Patient table
        int doctorID = 1;  // Assuming doctor ID 1 exists in the Doctor table
        String medication = "Paracetamol";
        String dosage = "500mg";
        String frequency = "Twice daily";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7); // Prescription ends after 7 days

        // Insert prescription into database
        if (insertPrescription(patientID, doctorID, medication, dosage, frequency, startDate, endDate)) {
            System.out.println("Prescription inserted successfully");
        } else {
            System.out.println("Failed to insert prescription");
        }
    }

    private static boolean insertPrescription(int patientID, int doctorID, String medication, String dosage, String frequency, LocalDate startDate, LocalDate endDate) {
        String sql = "INSERT INTO Prescription (patientID, doctorID, medication, dosage, frequency, startDate, endDate) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, patientID);
            pstmt.setInt(2, doctorID);
            pstmt.setString(3, medication);
            pstmt.setString(4, dosage);
            pstmt.setString(5, frequency);
            pstmt.setObject(6, startDate);
            pstmt.setObject(7, endDate);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
