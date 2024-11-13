import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import org.jdatepicker.impl.*;

public class MakePaymentPanel extends JPanel {
    private int patientID;
    private String firstName;
    private JComboBox<String> paymentMethodComboBox;
    private JPanel creditCardPanel;
    private JPanel chequePanel;
    private JTextField cardNumberField;
    private JTextField cardholderNameField;
    private JDatePickerImpl expiryDatePicker;
    private JTextField chequeNumberField;
    private JComboBox<String> bankNameComboBox;
    private final double paymentAmount = 25000.0;

    public MakePaymentPanel(int patientID, String firstName) {
        this.patientID = patientID;
        this.firstName = firstName;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Payment Method Label and ComboBox
        JLabel paymentMethodLabel = new JLabel("Select Payment Method:");
        paymentMethodComboBox = new JComboBox<>(new String[]{"Cash", "Credit Card", "Cheque"});
        paymentMethodComboBox.addActionListener(new PaymentMethodChangeListener());

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        add(paymentMethodLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(paymentMethodComboBox, gbc);

        // Credit Card Panel
        creditCardPanel = new JPanel(new GridBagLayout());
        creditCardPanel.setBorder(BorderFactory.createTitledBorder("Credit Card Details"));
        GridBagConstraints ccGbc = new GridBagConstraints();
        ccGbc.insets = new Insets(5, 5, 5, 5);

        JLabel cardNumberLabel = new JLabel("Card Number:");
        cardNumberField = new JTextField(16);

        ccGbc.gridx = 0;
        ccGbc.gridy = 0;
        creditCardPanel.add(cardNumberLabel, ccGbc);

        ccGbc.gridx = 1;
        ccGbc.gridy = 0;
        creditCardPanel.add(cardNumberField, ccGbc);

        JLabel cardholderNameLabel = new JLabel("Cardholder Name:");
        cardholderNameField = new JTextField(20);

        ccGbc.gridx = 0;
        ccGbc.gridy = 1;
        creditCardPanel.add(cardholderNameLabel, ccGbc);

        ccGbc.gridx = 1;
        ccGbc.gridy = 1;
        creditCardPanel.add(cardholderNameField, ccGbc);

        JLabel expiryDateLabel = new JLabel("Expiry Date:");
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        expiryDatePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        ccGbc.gridx = 0;
        ccGbc.gridy = 2;
        creditCardPanel.add(expiryDateLabel, ccGbc);

        ccGbc.gridx = 1;
        ccGbc.gridy = 2;
        creditCardPanel.add(expiryDatePicker, ccGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(creditCardPanel, gbc);

        // Cheque Panel
        chequePanel = new JPanel(new GridBagLayout());
        chequePanel.setBorder(BorderFactory.createTitledBorder("Cheque Details"));
        GridBagConstraints chGbc = new GridBagConstraints();
        chGbc.insets = new Insets(5, 5, 5, 5);

        JLabel chequeNumberLabel = new JLabel("Cheque Number:");
        chequeNumberField = new JTextField(20);

        chGbc.gridx = 0;
        chGbc.gridy = 0;
        chequePanel.add(chequeNumberLabel, chGbc);

        chGbc.gridx = 1;
        chGbc.gridy = 0;
        chequePanel.add(chequeNumberField, chGbc);

        JLabel bankNameLabel = new JLabel("Bank Name:");
        bankNameComboBox = new JComboBox<>(new String[]{
                "Access Bank Plc", "Citibank Nigeria Limited", "Ecobank Nigeria Plc",
                "Fidelity Bank Plc", "First Bank Nigeria Limited", "First City Monument Bank Plc",
                "Globus Bank Limited", "Guaranty Trust Bank Plc", "Heritage Banking Company Ltd.",
                "Keystone Bank Limited", "Optimus Bank", "Parallex Bank Ltd",
                "Polaris Bank Plc", "Premium Trust Bank", "Providus Bank",
                "Signature Bank Limited", "Stanbic IBTC Bank Plc", "Standard Chartered Bank Nigeria Ltd.",
                "Sterling Bank Plc", "SunTrust Bank Nigeria Limited", "Titan Trust Bank Ltd",
                "Union Bank of Nigeria Plc", "United Bank For Africa Plc", "Unity Bank Plc",
                "Wema Bank Plc", "Zenith Bank Plc"
        });

        chGbc.gridx = 0;
        chGbc.gridy = 1;
        chequePanel.add(bankNameLabel, chGbc);

        chGbc.gridx = 1;
        chGbc.gridy = 1;
        chequePanel.add(bankNameComboBox, chGbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(chequePanel, gbc);

        // Confirm Button
        JButton confirmButton = new JButton("Confirm Payment");
        confirmButton.addActionListener(new ConfirmButtonListener());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(confirmButton, gbc);

        updatePanelVisibility();
    }

    private void updatePanelVisibility() {
        String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
        creditCardPanel.setVisible("Credit Card".equals(selectedMethod));
        chequePanel.setVisible("Cheque".equals(selectedMethod));
        if ("Cash".equals(selectedMethod)) {
            JOptionPane.showMessageDialog(MakePaymentPanel.this, "NOTE: PAYMENT WOULD BE MADE AT OUR IKOYI OFFICE BEFORE THE APPOINTMENT BEGINS.", "Cash Payment", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private class PaymentMethodChangeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            updatePanelVisibility();
        }
    }

    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedMethod = (String) paymentMethodComboBox.getSelectedItem();
            if ("Cash".equals(selectedMethod)) {
                int confirmation = JOptionPane.showConfirmDialog(MakePaymentPanel.this, "Do you wish to continue?", "Cash Payment Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    insertPaymentRecord("Cash", null, null, null, null, null);
                }
            } else if ("Credit Card".equals(selectedMethod)) {
                String cardNumber = cardNumberField.getText();
                String cardholderName = cardholderNameField.getText();
                Date expiryDate = (Date) expiryDatePicker.getModel().getValue();

                if (cardNumber.length() != 16 || !cardNumber.matches("\\d+")) {
                    JOptionPane.showMessageDialog(MakePaymentPanel.this, "Invalid credit card number. It must be 16 digits.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (expiryDate == null || expiryDate.before(new Date())) {
                    JOptionPane.showMessageDialog(MakePaymentPanel.this, "Invalid expiry date. It must be a future date.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                insertPaymentRecord("Credit Card", cardNumber, cardholderName, expiryDate, null, null);
            } else if ("Cheque".equals(selectedMethod)) {
                String chequeNumber = chequeNumberField.getText();
                String bankName = (String) bankNameComboBox.getSelectedItem();
                insertPaymentRecord("Cheque", null, null, null, chequeNumber, bankName);
            }
        }
    }

    private void insertPaymentRecord(String paymentMethod, String cardNumber, String cardholderName, Date expiryDate, String chequeNumber, String bankName) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_hope_hospital", "root", "Natedekidd_01")) {
            String query = "INSERT INTO Billing (patientID, amount, billingDate, paymentStatus, paymentMethod, creditCardNumber, cardholderName, creditCardExpiryDate, chequeNumber, bankName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, patientID);
            pstmt.setDouble(2, paymentAmount);
            pstmt.setDate(3, new java.sql.Date(new java.util.Date().getTime()));
            pstmt.setString(4, "Paid");
            pstmt.setString(5, paymentMethod);
            if ("Credit Card".equals(paymentMethod)) {
                pstmt.setString(6, cardNumber);
                pstmt.setString(7, cardholderName);
                pstmt.setDate(8, new java.sql.Date(expiryDate.getTime()));
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            } else if ("Cheque".equals(paymentMethod)) {
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.DATE);
                pstmt.setString(9, chequeNumber);
                pstmt.setString(10, bankName);

            } else {
                pstmt.setNull(6, Types.VARCHAR);
                pstmt.setNull(7, Types.VARCHAR);
                pstmt.setNull(8, Types.DATE);
                pstmt.setNull(9, Types.VARCHAR);
                pstmt.setNull(10, Types.VARCHAR);
            }
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(MakePaymentPanel.this, "Payment successful. \n Appointment Booked successfully");

            // Close the payment frame or panel upon successful payment
            Window parentWindow = SwingUtilities.windowForComponent(MakePaymentPanel.this);
            if (parentWindow != null) {
                parentWindow.dispose();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(MakePaymentPanel.this, "Error processing payment: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private class DateLabelFormatter extends JFormattedTextField.AbstractFormatter {
        private final String datePattern = "yyyy-MM-dd";
        private final SimpleDateFormat dateFormatter = new SimpleDateFormat(datePattern);

        @Override
        public Object stringToValue(String text) throws ParseException {
            return dateFormatter.parseObject(text);
        }

        @Override
        public String valueToString(Object value) throws ParseException {
            if (value != null) {
                java.util.Calendar cal = (java.util.Calendar) value;
                return dateFormatter.format(cal.getTime());
            }
            return "";
        }
    }
}

