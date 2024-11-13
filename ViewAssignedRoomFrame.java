import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class ViewAssignedRoomFrame extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private int patientID;
    private String firstName;
    private JLabel roomNumberLabel;
    private JLabel roomTypeLabel;
    private JLabel availabilityStatusLabel;

    public ViewAssignedRoomFrame(int patientID, String firstName) {
        this.patientID = patientID;
        this.firstName = firstName;
        int w = 600;
        int h = 400;

        setBounds(0, 0, w, h);
        setLayout(new GridLayout(3, 2, 10, 10));

        roomNumberLabel = new JLabel("Room Number: ");
        roomTypeLabel = new JLabel("Room Type: ");
        availabilityStatusLabel = new JLabel("Availability Status: ");

        add(new JLabel("Room Number:"));
        add(roomNumberLabel);
        add(new JLabel("Room Type:"));
        add(roomTypeLabel);
        add(new JLabel("Availability Status:"));
        add(availabilityStatusLabel);

        fetchAndDisplayRoomDetails();
    }

    private void fetchAndDisplayRoomDetails() {
        String sql = "SELECT roomNumber, type, availabilityStatus FROM Room WHERE patientID = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientID);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String roomNumber = rs.getString("roomNumber");
                String type = rs.getString("type");
                String availabilityStatus = rs.getString("availabilityStatus");

                roomNumberLabel.setText(roomNumber);
                roomTypeLabel.setText(type);
                availabilityStatusLabel.setText(availabilityStatus);
            } else {
                JOptionPane.showMessageDialog(this, "No room assigned to this patient.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
