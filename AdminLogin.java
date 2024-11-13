import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdminLogin extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";  // Database URL
    private static final String DB_USER = "root";  // Database username
    private static final String DB_PASSWORD = "Natedekidd_01";  // Database password

    private JTextField usernameText;
    private JPasswordField passwordText;
    private JButton verifyButton;
    private String initialUsername;
    private String initialPassword;

    public AdminLogin(String initialUsername, String initialPassword) {
        this.initialUsername = initialUsername;
        this.initialPassword = initialPassword;

        setTitle("Admin Verification");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Title Label
        JLabel titleLabel = new JLabel("Admin Verification", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        add(titleLabel, gbc);

        // Username Label and Text Field
        JLabel userLabel = new JLabel("Username:");
        usernameText = new JTextField(20);
        usernameText.setText(initialUsername); // Pre-fill with initial username
        usernameText.setEditable(false); // Make the text field read-only
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        add(userLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameText, gbc);

        // Password Label and Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordText = new JPasswordField(20);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordText, gbc);

        // Verify Button
        verifyButton = new JButton("Verify");
        verifyButton.addActionListener(new VerifyButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(verifyButton, gbc);

        setVisible(true);
    }

    // ActionListener for the "Verify" button
    private class VerifyButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredPassword = new String(passwordText.getPassword());

            // Verify credentials
            if (checkAdminCredentials("admin", enteredPassword)) {
                JOptionPane.showMessageDialog(AdminLogin.this, "Verification successful");

                // Grant access to admin dashboard
                dispose(); // Close the verification form
                new AdminApp("admin"); // Open the admin dashboard
            } else {
                JOptionPane.showMessageDialog(AdminLogin.this, "Verification failed. Please try again.");
            }
        }

        // Method to check the admin credentials in the database
        private boolean checkAdminCredentials(String username, String password) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                String query = "SELECT password FROM Admin_credentials WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(password); // Compare entered password with stored password
                    }
                }
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
            return false;
        }
    }

    // Main method to launch the AdminVerificationForm with a default username and password
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminLogin("admin", "password123"));
    }
}
