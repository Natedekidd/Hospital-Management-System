import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertDepartmentData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    public static void main(String[] args) {
        insertDepartment("Cardiology", "Building A, Floor 2", "Dr. Smith");
        insertDepartment("Neurology", "Building B, Floor 3", "Dr. Johnson");
        insertDepartment("Pediatrics", "Building C, Floor 1", "Dr. Brown");
        insertDepartment("Gynaecology", "Building C, Floor 2", "Dr. David");
        insertDepartment("Physiotherapy", "Building C, Floor 2", "Dr. Ben");
        insertDepartment("Pharmacy", "Building D, Floor 1", "Dr. Simon");
        insertDepartment("Dentistry", "Building D, Floor 3", "Dr. Peter");

    }

    public static void insertDepartment(String departmentName, String location, String headOfDepartment) {
        String query = "INSERT INTO Department (departmentName, location, headOfDepartment) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, departmentName);
            pstmt.setString(2, location);
            pstmt.setString(3, headOfDepartment);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Inserted successfully: " + departmentName);
            } else {
                System.out.println("Insert failed for: " + departmentName);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

