import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AssignRooms extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private JTextField patientIDField;
    private JTextField roomNumberField;
    private JComboBox<String> roomTypeBox;
    private JButton assignButton;

    public AssignRooms() {
        setLayout(new GridLayout(4, 2, 10, 10));
        int w = 600;
        int h = 400;
        setBounds(0, 0, w, h);

        add(new JLabel("Patient ID:"));
        patientIDField = new JTextField();
        add(patientIDField);

        add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        add(roomNumberField);

        add(new JLabel("Room Type:"));
        roomTypeBox = new JComboBox<>(new String[]{"General", "Semi-Private", "Private"});
        add(roomTypeBox);

        assignButton = new JButton("Assign Room");
        add(assignButton);

        assignButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignRoom();
            }
        });
    }

    private void assignRoom() {
        int patientID = Integer.parseInt(patientIDField.getText());
        String roomNumber = roomNumberField.getText();
        String roomType = (String) roomTypeBox.getSelectedItem();

        if (isRoomAvailable(roomNumber)) {
            String sql = "UPDATE Room SET patientID = ?, type = ?, availabilityStatus = 'Occupied' WHERE roomNumber = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, patientID);
                pstmt.setString(2, roomType);
                pstmt.setString(3, roomNumber);
                int updated = pstmt.executeUpdate();

                if (updated > 0) {
                    JOptionPane.showMessageDialog(this, "Room assigned successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to assign room. Please check the room number.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Room is not available.");
        }
    }

    private boolean isRoomAvailable(String roomNumber) {
        String sql = "SELECT availabilityStatus FROM Room WHERE roomNumber = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, roomNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("availabilityStatus");
                return "Available".equalsIgnoreCase(status);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
}
