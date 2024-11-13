import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class PatientLogin extends JFrame {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/new_hope_hospital";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Natedekidd_01";

    private JTextField usernameText;
    private JPasswordField passwordText;

    public PatientLogin() {
        setTitle("New Hope Hospital - Patient Login");
        setSize(400, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel hospitalLabel = new JLabel("New Hope Hospital", SwingConstants.CENTER);
        hospitalLabel.setFont(new Font("Serif", Font.BOLD, 24));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 20, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(hospitalLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        usernameText = new JTextField(20);

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

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new LoginButtonListener());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(loginButton, gbc);

        JLabel signupLabel = new JLabel("Don't have an account? Sign up", SwingConstants.CENTER);
        signupLabel.setForeground(Color.BLUE);
        signupLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        signupLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new PatientRegistrationFrame();  // Opens registration frame
                dispose();
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(signupLabel, gbc);

        setVisible(true);
    }

    private class LoginButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredUsername = usernameText.getText();
            String enteredPassword = new String(passwordText.getPassword());

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                JOptionPane.showMessageDialog(PatientLogin.this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (checkPatientLogin(enteredUsername, enteredPassword)) {
                int id = getPatientID(enteredUsername);
                String firstName = getFirstName(enteredUsername);

                JOptionPane.showMessageDialog(PatientLogin.this, "Login successful");
                new App(id, firstName);  // Open patient application with ID and first name
                dispose();
            } else {
                JOptionPane.showMessageDialog(PatientLogin.this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private boolean checkPatientLogin(String username, String password) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT password FROM Patient WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        return storedPassword.equals(password);
                    }
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

        private String getFirstName(String username) {
            String firstName = null;
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
                String query = "SELECT firstName FROM Patient WHERE username = ?";
                try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                    pstmt.setString(1, username);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        firstName = rs.getString("firstName");
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return firstName;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(PatientLogin::new);
    }
}
