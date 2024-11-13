import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewPatientRecords extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    public ViewPatientRecords(int doctorID, String doctorName) {
//        setTitle("View Patient Records - " + doctorName);
        setSize(800, 600);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve patient records for the doctor
        String query = "SELECT m.recordID, p.firstName AS Patient, d.firstName AS Doctor, m.diagnosis, m.treatment, m.dateOfRecord " +
                "FROM MedicalRecord m " +
                "INNER JOIN Patient p ON m.patientID = p.patientID " +
                "INNER JOIN Doctor d ON m.doctorID = d.doctorID " +
                "WHERE m.doctorID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorID);
            ResultSet rs = pstmt.executeQuery();

            // Create a table to display patient records
            JTable patientRecordsTable = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(patientRecordsTable);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve patient records.");
        }

        setVisible(true);
    }

    // Method to build table model from ResultSet
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();

        // Column names
        int columnCount = metaData.getColumnCount();
        String[] columnNames = new String[columnCount];
        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
            columnNames[columnIndex - 1] = metaData.getColumnName(columnIndex);
        }

        // Data of the table
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.setColumnIdentifiers(columnNames);
        while (rs.next()) {
            Object[] rowData = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                rowData[i] = rs.getObject(i + 1);
            }
            tableModel.addRow(rowData);
        }
        return tableModel;
    }

//    public static void main(String[] args) {
//        // Example usage:
//        int doctorID = 123; // Example doctor ID
//        String firstName = "John"; // Example doctor's first name
//        SwingUtilities.invokeLater(() -> new ViewPatientRecords(doctorID, firstName));
//    }
}

