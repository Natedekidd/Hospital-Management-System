import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class PatientRegistrationFrame extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Natedekidd_01";

    private JTextField firstNameField, lastNameField, usernameField, emailField, phoneField, emergencyContactField, addressField, insuranceField;
    private JPasswordField passwordField;
    private JComboBox<String> genderBox, bloodTypeBox;
    private JFormattedTextField dobField;

    public PatientRegistrationFrame() {
        setTitle("Patient Registration");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Labels and Input Fields Setup
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addLabelAndField("First Name:", firstNameField = new JTextField(20), gbc, 0);
        addLabelAndField("Last Name:", lastNameField = new JTextField(20), gbc, 1);
        addLabelAndField("Username:", usernameField = new JTextField(20), gbc, 2);
        addLabelAndField("Password:", passwordField = new JPasswordField(20), gbc, 3);
        addLabelAndField("Date of Birth:", dobField = new JFormattedTextField(), gbc, 4);
        dobField.setToolTipText("YYYY-MM-DD");

        String[] genders = {"Male", "Female", "Other"};
        addLabelAndField("Gender:", genderBox = new JComboBox<>(genders), gbc, 5);

        addLabelAndField("Email:", emailField = new JTextField(20), gbc, 6);
        addLabelAndField("Phone Number:", phoneField = new JTextField(20), gbc, 7);
        addLabelAndField("Emergency Contact:", emergencyContactField = new JTextField(20), gbc, 8);
        addLabelAndField("Address:", addressField = new JTextField(20), gbc, 9);

        String[] bloodTypes = {"A", "B", "AB", "O"};
        addLabelAndField("Blood Type:", bloodTypeBox = new JComboBox<>(bloodTypes), gbc, 10);

        addLabelAndField("Insurance Details:", insuranceField = new JTextField(20), gbc, 11);

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new RegisterButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 12;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        setVisible(true);
    }

    private void addLabelAndField(String labelText, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        add(new JLabel(labelText), gbc);

        gbc.gridx = 1;
        add(field, gbc);
    }

    private class RegisterButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String dob = dobField.getText().trim();
            String gender = (String) genderBox.getSelectedItem();
            String email = emailField.getText().trim();
            String phoneNumber = phoneField.getText().trim();
            String emergencyContact = emergencyContactField.getText().trim();
            String address = addressField.getText().trim();
            String bloodType = (String) bloodTypeBox.getSelectedItem();
            String insuranceDetails = insuranceField.getText().trim();

            // Validations
            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() ||
                    dob.isEmpty() || email.isEmpty() || phoneNumber.isEmpty() || emergencyContact.isEmpty() ||
                    address.isEmpty() || insuranceDetails.isEmpty()) {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Pattern.matches("\\d{4}-\\d{2}-\\d{2}", dob)) {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Please enter date of birth in YYYY-MM-DD format.", "Invalid DOB", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Pattern.matches("^\\d{10,15}$", phoneNumber) || !Pattern.matches("^\\d{10,15}$", emergencyContact)) {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Phone numbers must contain only digits and be between 10 and 15 characters.", "Invalid Phone Number", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!Pattern.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", email)) {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Please enter a valid email address.", "Invalid Email", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Insert into database
            if (registerPatient(firstName, lastName, username, password, dob, gender, email, phoneNumber, emergencyContact, address, bloodType, insuranceDetails)) {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Registration Successful!");
                new App(getPatientID(username), firstName);  // Open App with patient ID and first name
                dispose();
            } else {
                JOptionPane.showMessageDialog(PatientRegistrationFrame.this, "Registration failed. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean registerPatient(String firstName, String lastName, String username, String password, String dob,
                                        String gender, String email, String phoneNumber, String emergencyContact,
                                        String address, String bloodType, String insuranceDetails) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "INSERT INTO Patient (firstName, lastName, username, password, dateofBirth, gender, email, contactNumber, emergencyContact, address, bloodType, insuranceDetails) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, firstName);
                    pstmt.setString(2, lastName);
                    pstmt.setString(3, username);
                    pstmt.setString(4, password);
                    pstmt.setDate(5, Date.valueOf(dob));
                    pstmt.setString(6, gender);
                    pstmt.setString(7, email);
                    pstmt.setString(8, phoneNumber);
                    pstmt.setString(9, emergencyContact);
                    pstmt.setString(10, address);
                    pstmt.setString(11, bloodType);
                    pstmt.setString(12, insuranceDetails);

                    return pstmt.executeUpdate() > 0;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        private int getPatientID(String username) {
            int id = -1;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT patientID FROM Patient WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        id = rs.getInt("patientID");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return id;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientRegistrationFrame::new);
    }
}
