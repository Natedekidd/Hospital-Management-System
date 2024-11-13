import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewPatientTreatment extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    public ViewPatientTreatment(int patientID, String patientName) {
//        setTitle("Patient Treatments - " + patientName);
        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve treatments for the patient
        String query = "SELECT t.treatmentID, CONCAT(d.firstName, ' ', d.lastName) AS doctorName, " +
                "t.treatmentDetails, t.treatmentDate " +
                "FROM Treatment t " +
                "JOIN Doctor d ON t.doctorID = d.doctorID " +
                "WHERE t.patientID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();

            // Create a table to display treatments
            JTable treatmentTable = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(treatmentTable);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve treatments.");
        }

        setVisible(true);
    }

    // Method to build table model from ResultSet
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = {"Treatment ID", "Doctor Name", "Treatment Details", "Treatment Date"};

        // Data of the table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            rowData[0] = rs.getObject(1); // Treatment ID
            rowData[1] = rs.getString("doctorName"); // Doctor Name
            rowData[2] = rs.getObject(3); // Treatment Details
            rowData[3] = rs.getObject(4); // Treatment Date
            tableModel.addRow(rowData);
        }
        return tableModel;
    }




//    public static void main(String[] args) {
//        // Example usage:
//        int patientID = 1; // Example patient ID
//        String patientName = "John Doe"; // Example patient name
//        SwingUtilities.invokeLater(() -> new ViewPatientTreatment(patientID, patientName));
//    }
}

