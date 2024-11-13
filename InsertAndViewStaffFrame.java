import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class InsertAndViewStaffFrame extends JPanel {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField roleField;
    private JTextField contactNumberField;
    private JTextField emailField;
    private JComboBox<String> departmentComboBox;
    private JComboBox<String> staffComboBox;

    public InsertAndViewStaffFrame() {
//        setTitle("Staff Management");
        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        inputPanel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        inputPanel.add(firstNameField);

        inputPanel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        inputPanel.add(lastNameField);

        inputPanel.add(new JLabel("Role:"));
        roleField = new JTextField();
        inputPanel.add(roleField);

        inputPanel.add(new JLabel("Contact Number:"));
        contactNumberField = new JTextField();
        inputPanel.add(contactNumberField);

        inputPanel.add(new JLabel("Email:"));
        emailField = new JTextField();
        inputPanel.add(emailField);

        inputPanel.add(new JLabel("Department:"));
        departmentComboBox = new JComboBox<>(getDepartmentOptions());
        inputPanel.add(departmentComboBox);

        JButton insertButton = new JButton("Insert");
        insertButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                insertStaff();
            }
        });
        inputPanel.add(insertButton);

        JButton viewButton = new JButton("View Staff");
        viewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewStaff();
            }
        });
        inputPanel.add(viewButton);

        add(inputPanel, BorderLayout.WEST);

        staffComboBox = new JComboBox<>();
        populateStaffComboBox();
        add(staffComboBox, BorderLayout.NORTH);

        JButton removeButton = new JButton("Remove Staff");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeStaff();
            }
        });
        add(removeButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String[] getDepartmentOptions() {
        return new String[]{"Department 1", "Department 2", "Department 3"};
    }

    private void insertStaff() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String role = roleField.getText().trim();
        String contactNumber = contactNumberField.getText().trim();
        String email = emailField.getText().trim();
        String selectedDepartment = (String) departmentComboBox.getSelectedItem();
        if (selectedDepartment == null) {
            JOptionPane.showMessageDialog(this, "Please select a department.");
            return;
        }

        int departmentID = Integer.parseInt(selectedDepartment.split(":")[0].trim());

        if (firstName.isEmpty() || lastName.isEmpty() || role.isEmpty() || contactNumber.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Staff (firstName, lastName, role, contactNumber, email, departmentID) VALUES (?, ?, ?, ?, ?, ?)")) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, role);
            pstmt.setString(4, contactNumber);
            pstmt.setString(5, email);
            pstmt.setInt(6, departmentID);

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Staff member inserted successfully.");
                firstNameField.setText("");
                lastNameField.setText("");
                roleField.setText("");
                contactNumberField.setText("");
                emailField.setText("");
                departmentComboBox.setSelectedIndex(0);
            } else {
                JOptionPane.showMessageDialog(this, "Failed to insert staff member.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void viewStaff() {
        JFrame staffListFrame = new JFrame("Staff List");
        staffListFrame.setSize(600, 400);
        staffListFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        staffListFrame.setLocationRelativeTo(null);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        model.addColumn("Staff ID");
        model.addColumn("First Name");
        model.addColumn("Last Name");
        model.addColumn("Role");
        model.addColumn("Contact Number");
        model.addColumn("Email");
        model.addColumn("Department ID");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT * FROM Staff";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int staffID = rs.getInt("staffID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String role = rs.getString("role");
                String contactNumber = rs.getString("contactNumber");
                String email = rs.getString("email");
                int departmentID = rs.getInt("departmentID");

                model.addRow(new Object[]{staffID, firstName, lastName, role, contactNumber, email, departmentID});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        staffListFrame.add(scrollPane);

        staffListFrame.setVisible(true);
    }

    private void removeStaff() {
        String selectedItem = (String) staffComboBox.getSelectedItem();
        if (selectedItem != null) {
            int staffID = Integer.parseInt(selectedItem.split(":")[0].trim());
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Staff WHERE staffID = ?")) {

                pstmt.setInt(1, staffID);
                int rowsAffected = pstmt.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Staff member removed successfully.");
                    populateStaffComboBox();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to remove staff member.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "No staff member selected.");
        }
    }

    private void populateStaffComboBox() {
        staffComboBox.removeAllItems();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {

            String query = "SELECT staffID, firstName, lastName FROM Staff";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int staffID = rs.getInt("staffID");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String fullName = firstName + " " + lastName;
                staffComboBox.addItem(staffID + ": " + fullName);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(InsertAndViewStaffFrame::new);
//    }
}
