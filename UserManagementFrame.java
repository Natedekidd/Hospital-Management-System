import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UserManagementFrame extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private JComboBox<String> userComboBox;

    public UserManagementFrame() {
//        setTitle("User Management");
        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout());

        userComboBox = new JComboBox<>();
        populateUserComboBox();
        mainPanel.add(userComboBox, BorderLayout.NORTH);

        JButton viewButton = new JButton("View User Info");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewUserInfo();
            }
        });
        mainPanel.add(viewButton, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete User");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUser();
            }
        });
        mainPanel.add(deleteButton, BorderLayout.SOUTH);

        add(mainPanel);

        setVisible(true);
    }

    private void populateUserComboBox() {
        userComboBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT id, username FROM Patient";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int userID = rs.getInt("id");
                String username = rs.getString("username");
                userComboBox.addItem(userID + ": " + username);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewUserInfo() {
        String selectedItem = (String) userComboBox.getSelectedItem();
        if (selectedItem != null) {
            int userID = Integer.parseInt(selectedItem.split(":")[0].trim());
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Patient WHERE id = ?")) {

                pstmt.setInt(1, userID);
                ResultSet rs = pstmt.executeQuery();

                StringBuilder userInfo = new StringBuilder();
                if (rs.next()) {
                    userInfo.append("User ID: ").append(rs.getInt("id")).append("\n");
                    userInfo.append("First Name: ").append(rs.getString("first_name")).append("\n");
                    userInfo.append("Last Name: ").append(rs.getString("last_name")).append("\n");
                    userInfo.append("Email: ").append(rs.getString("email")).append("\n");
                    userInfo.append("Phone Number: ").append(rs.getString("phone_number")).append("\n");
                    userInfo.append("Username: ").append(rs.getString("username")).append("\n");
                    userInfo.append("Role: ").append(rs.getString("role")).append("\n");
                }
                JOptionPane.showMessageDialog(this, userInfo.toString(), "User Information", JOptionPane.INFORMATION_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No user selected.");
        }
    }

    private void deleteUser() {
        String selectedItem = (String) userComboBox.getSelectedItem();
        if (selectedItem != null) {
            int userID = Integer.parseInt(selectedItem.split(":")[0].trim());
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this user?", "Confirmation", JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                     PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Sign_Up_info WHERE id = ?")) {

                    pstmt.setInt(1, userID);
                    int rowsAffected = pstmt.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "User deleted successfully.");
                        populateUserComboBox(); // Update the combo box after removing user
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to delete user.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "No user selected.");
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(UserManagementFrame::new);
    }


