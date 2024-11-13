import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ViewAppointments extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    public ViewAppointments(int doctorID, String doctorName) {
//        setTitle("View Appointments - " + doctorName);
        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Retrieve appointments for the doctor using inner join
        //

        String query = "SELECT CONCAT(p.firstName, ' ', p.lastName) AS Patient, " +
                "CONCAT(d.firstName, ' ', d.lastName) AS Doctor, " +
                "a.appointmentDate, a.appointmentTime " +
                "FROM Appointment a " +
                "JOIN Patient p ON a.patientID = p.patientID " +
                "JOIN Doctor d ON a.doctorID = d.doctorID " +
                "WHERE a.doctorID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, doctorID);
            ResultSet rs = pstmt.executeQuery();

            // Create a table to display appointments
            JTable appointmentTable = new JTable(buildTableModel(rs));
            JScrollPane scrollPane = new JScrollPane(appointmentTable);
            add(scrollPane, BorderLayout.CENTER);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve appointments.");
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
//        SwingUtilities.invokeLater(() -> new ViewAppointments(doctorID, firstName));
//    }
}
