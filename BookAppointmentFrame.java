import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.jdatepicker.impl.*;

public class BookAppointmentFrame extends JPanel {
    private int patientID;
    private String firstName;
    private JComboBox<String> doctorComboBox;
    private JComboBox<String> timeSlotComboBox;
    private JDatePickerImpl datePicker;


    public BookAppointmentFrame(int patientID, String firstName) {
        this.patientID = patientID;
        this.firstName = firstName;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Doctor Label and ComboBox
        JLabel doctorLabel = new JLabel("Select Doctor:");
        doctorComboBox = new JComboBox<>();
        loadDoctors();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.EAST;
        add(doctorLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(doctorComboBox, gbc);

        // Date Label and DatePicker
        JLabel dateLabel = new JLabel("Select Date:");
        UtilDateModel dateModel = new UtilDateModel();
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");
        JDatePanelImpl datePanel = new JDatePanelImpl(dateModel, p);
        datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(datePicker, gbc);

        // Time Slot Label and ComboBox
        JLabel timeSlotLabel = new JLabel("Select Time Slot:");
        timeSlotComboBox = new JComboBox<>();

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(timeSlotLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(timeSlotComboBox, gbc);

        // Submit Button
        JButton submitButton = new JButton("Book Appointment");
        submitButton.addActionListener(new SubmitButtonListener());

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 0, 10, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        add(submitButton, gbc);

        doctorComboBox.addActionListener(e -> updateAvailableTimeSlots());
        datePicker.addActionListener(e -> updateAvailableTimeSlots());
        updateAvailableTimeSlots();


    }

    private void loadDoctors() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_hope_hospital", "root", "Natedekidd_01")) {
            String query = "SELECT doctorID, firstName, lastName FROM Doctor";
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
                while (rs.next()) {
                    int doctorID = rs.getInt("doctorID");
                    String doctorName = rs.getString("firstName") + " " + rs.getString("lastName");
                    doctorComboBox.addItem(doctorName);
                    doctorComboBox.putClientProperty(doctorName, doctorID);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void updateAvailableTimeSlots() {
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        if (selectedDoctor == null) return;

        int doctorID = (int) doctorComboBox.getClientProperty(selectedDoctor);

        java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
        if (selectedDate == null) return;

        Calendar cal = Calendar.getInstance();
        cal.setTime(selectedDate);
        cal.set(Calendar.HOUR_OF_DAY, 7); // Start from 7 AM
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        java.sql.Time startTime = new java.sql.Time(cal.getTimeInMillis());

        cal.set(Calendar.HOUR_OF_DAY, 19); // End at 7 PM
        java.sql.Time endTime = new java.sql.Time(cal.getTimeInMillis());

        List<String> availableTimeSlots = generateRandomTimeSlots(startTime, endTime);
        timeSlotComboBox.removeAllItems();
        availableTimeSlots.forEach(timeSlotComboBox::addItem);
    }

    private List<String> generateRandomTimeSlots(java.sql.Time startTime, java.sql.Time endTime) {
        List<String> timeSlots = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        cal.setTime(startTime);
        Random random = new Random();
        while (cal.getTime().before(endTime)) {
            cal.add(Calendar.MINUTE, random.nextInt(60)); // Add random minutes
            if (cal.getTime().before(endTime)) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                timeSlots.add(sdf.format(cal.getTime()));
            }
        }
        return timeSlots;

    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedDoctor = (String) doctorComboBox.getSelectedItem();
            int doctorID = (int) doctorComboBox.getClientProperty(selectedDoctor);
            java.util.Date selectedDate = (java.util.Date) datePicker.getModel().getValue();
            String selectedTime = (String) timeSlotComboBox.getSelectedItem();

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            java.sql.Time sqlTime = null;
            try {
                sqlTime = new java.sql.Time(timeFormat.parse(selectedTime).getTime());
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            java.sql.Date sqlDate = new java.sql.Date(selectedDate.getTime());

            String costMessage = "The cost of booking your appointment is ₦25,000.";
            int costConfirmation = JOptionPane.showConfirmDialog(BookAppointmentFrame.this, costMessage, "Appointment Cost", JOptionPane.OK_CANCEL_OPTION);
            if (costConfirmation == JOptionPane.OK_OPTION) {
                int confirmation = JOptionPane.showConfirmDialog(BookAppointmentFrame.this, "Do you wish to continue?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirmation == JOptionPane.YES_OPTION) {
                    // Insert the appointment into the database
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/new_hope_hospital", "root", "Natedekidd_01")) {
//                        new makePaymentPanel(patientID,firstName);
//                        makePaymentFunction(patientID);
                        App i = new App(patientID,firstName);
                        i.makePaymentFunction(patientID);
                        String query = "INSERT INTO Appointment (patientID, doctorID, appointmentDate, appointmentTime, status) VALUES (?, ?, ?, ?, ?)";
                        PreparedStatement pstmt = conn.prepareStatement(query);

                        pstmt.setInt(1, patientID);
                        pstmt.setInt(2, doctorID);
                        pstmt.setDate(3, sqlDate);
                        pstmt.setTime(4, sqlTime);
                        pstmt.setString(5, "Scheduled");
                        pstmt.executeUpdate();


//                        JOptionPane.showMessageDialog(BookAppointmentFrame.this, "Appointment booked successfully.");

                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    // User chose not to continue
                    JOptionPane.showMessageDialog(BookAppointmentFrame.this, "Appointment booking canceled.");
                }
            } else {
                // User canceled the cost confirmation dialog
                JOptionPane.showMessageDialog(BookAppointmentFrame.this, "Appointment booking canceled.");
            }
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
