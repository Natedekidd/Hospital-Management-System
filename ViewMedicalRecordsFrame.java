import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.sql.*;

public class ViewMedicalRecordsFrame extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private int patientID;
    private String firstName;
    private JTable recordsTable;
    private DefaultTableModel tableModel;

    public ViewMedicalRecordsFrame(int patientID, String firstName) {
        this.patientID = patientID;
        this.firstName = firstName;

        setLayout(new BorderLayout());

        // Initialize table model
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Record Type");
        tableModel.addColumn("Details");
        tableModel.addColumn("Doctor");
        tableModel.addColumn("Date");

        // Initialize table
        recordsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(recordsTable);
        add(scrollPane, BorderLayout.CENTER);

        TableColumnModel columnModel = recordsTable.getColumnModel();
//        columnModel.getColumn(1).setPreferredWidth(120);
        columnModel.getColumn(3).setPreferredWidth(120); // Date


        // Fetch and display records
        fetchAndDisplayRecords();
    }

    private void fetchAndDisplayRecords() {
        fetchMedicalRecords();
        fetchPrescriptions();
    }

    private void fetchMedicalRecords() {
        String sql = "SELECT m.diagnosis, m.treatment, d.firstName AS doctorFirstName, d.lastName AS doctorLastName, m.dateOfRecord " +
                "FROM MedicalRecord m " +
                "JOIN Doctor d ON m.doctorID = d.doctorID " +
                "WHERE m.patientID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String diagnosis = rs.getString("diagnosis");
                String treatment = rs.getString("treatment");
                String doctorName = rs.getString("doctorFirstName") + " " + rs.getString("doctorLastName");
                String dateOfRecord = rs.getString("dateOfRecord");

                tableModel.addRow(new Object[]{"Medical Record", diagnosis + ", " + treatment, doctorName, dateOfRecord});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void fetchPrescriptions() {
        String sql = "SELECT p.medication, p.dosage, p.frequency, d.firstName AS doctorFirstName, d.lastName AS doctorLastName, p.startDate, p.endDate " +
                "FROM Prescription p " +
                "JOIN Doctor d ON p.doctorID = d.doctorID " +
                "WHERE p.patientID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String medication = rs.getString("medication");
                String dosage = rs.getString("dosage");
                String frequency = rs.getString("frequency");
                String doctorName = rs.getString("doctorFirstName") + " " + rs.getString("doctorLastName");
                String startDate = rs.getString("startDate");
                String endDate = rs.getString("endDate");

                tableModel.addRow(new Object[]{"Prescription", medication + ", " + dosage + ", " + frequency, doctorName, startDate + " to " + endDate});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
