import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class DoctorRegistrationFrame extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root"; // replace with your MySQL username
    private static final String DB_PASSWORD = "Natedekidd_01"; // replace with your MySQL password

    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField specializationField;
    private JTextField contactNumberField;
    private JTextField emailField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> departmentComboBox;

    public DoctorRegistrationFrame() {
        setTitle("Doctor Registration");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Set frame icon
        Image icont = Toolkit.getDefaultToolkit().getImage("C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0014.jpg");
        setIconImage(icont);

        // Header image
        ImageIcon iconImage = new ImageIcon("C:\\Users\\imma_\\IdeaProjects\\JJ\\src\\IMG-20240603-WA0014.jpg");
        Image scaledImage = iconImage.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        JLabel topDisplay = new JLabel(new ImageIcon(scaledImage));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.NORTH;
        add(topDisplay, gbc);

        // Title label
        JLabel titleLabel = new JLabel("Doctor Registration", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        add(titleLabel, gbc);

        // First Name field
        addField("First Name:", firstNameField = new JTextField(20), gbc, 2);

        // Last Name field
        addField("Last Name:", lastNameField = new JTextField(20), gbc, 3);

        // Specialization field
        addField("Specialization:", specializationField = new JTextField(20), gbc, 4);

        // Contact Number field
        addField("Contact Number:", contactNumberField = new JTextField(20), gbc, 5);

        // Email field
        addField("Email:", emailField = new JTextField(20), gbc, 6);

        // Username field
        addField("Username:", usernameField = new JTextField(20), gbc, 7);

        // Password field
        addField("Password:", passwordField = new JPasswordField(20), gbc, 8);

        // Department selection field
        JLabel departmentLabel = new JLabel("Department:");
        departmentComboBox = new JComboBox<>();
        populateDepartmentComboBox();
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = 9;
        add(departmentLabel, gbc);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(departmentComboBox, gbc);

        // Register button
        JButton registerButton = new JButton("Register");
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        add(registerButton, gbc);

        registerButton.addActionListener(e -> registerDoctor());

        setVisible(true);
    }

    // Method to add form fields to the frame
    private void addField(String labelText, JTextField textField, GridBagConstraints gbc, int gridY) {
        JLabel label = new JLabel(labelText);
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        gbc.gridx = 0;
        gbc.gridy = gridY;
        add(label, gbc);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridx = 1;
        add(textField, gbc);
    }

    // Populate department combo box with department names and IDs
    private void populateDepartmentComboBox() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT departmentID, departmentName FROM Department";
            PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int departmentID = rs.getInt("departmentID");
                String departmentName = rs.getString("departmentName");
                departmentComboBox.addItem(departmentName + " (ID: " + departmentID + ")");
                departmentComboBox.putClientProperty(departmentName + " (ID: " + departmentID + ")", departmentID);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    // Register the doctor in the database
    private void registerDoctor() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String specialization = specializationField.getText();
        String contactNumber = contactNumberField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String selectedDepartment = (String) departmentComboBox.getSelectedItem();
        int departmentID = (int) departmentComboBox.getClientProperty(selectedDepartment);

        // Validate email, phone number, and password
        if (!isValidEmail(email)) {
            JOptionPane.showMessageDialog(this, "Invalid email format");
            return;
        }
        if (!isValidPhoneNumber(contactNumber)) {
            JOptionPane.showMessageDialog(this, "Phone number must be 11 digits and start with 0");
            return;
        }
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password cannot be empty");
            return;
        }

        String query = "INSERT INTO Doctor (firstName, lastName, specialization, contactNumber, email, username, password, departmentID) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, specialization);
            pstmt.setString(4, contactNumber);
            pstmt.setString(5, email);
            pstmt.setString(6, username);
            pstmt.setString(7, password);
            pstmt.setInt(8, departmentID);
            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            int doctorID = -1;
            if (rs.next()) {
                doctorID = rs.getInt(1);
            }

            if (doctorID != -1) {
                JOptionPane.showMessageDialog(this, "Doctor registered successfully");
                new DoctorApp(doctorID, firstName);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to retrieve doctorID", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Registration failed");
        }
    }

    // Email validation
    private boolean isValidEmail(String email) {
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(emailRegex, email);
    }

    // Phone number validation
    private boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^0\\d{10}$";
        return Pattern.matches(phoneRegex, phoneNumber);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DoctorRegistrationFrame::new);
    }
}
