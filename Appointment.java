import java.time.LocalDate;
import java.time.LocalTime;
import java.util.regex.Pattern;

public class Appointment {
    private int appointmentID;
    private int patientID;
    private int doctorID;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private String status;

    // Constructor
    public Appointment(int patientID, int doctorID, LocalDate appointmentDate, LocalTime appointmentTime, String status) {
        if (!validatePatientID(patientID) || !validateDoctorID(doctorID) || !validateDate(appointmentDate) || !validateTime(appointmentTime) || !validateStatus(status)) {
            throw new IllegalArgumentException("Invalid appointment details");
        }
        this.patientID = patientID;
        this.doctorID = doctorID;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    // Getters and Setters
    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public int getPatientID() {
        return patientID;
    }

    public void setPatientID(int patientID) {
        if (!validatePatientID(patientID)) {
            throw new IllegalArgumentException("Invalid patient ID");
        }
        this.patientID = patientID;
    }

    public int getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(int doctorID) {
        if (!validateDoctorID(doctorID)) {
            throw new IllegalArgumentException("Invalid doctor ID");
        }
        this.doctorID = doctorID;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        if (!validateDate(appointmentDate)) {
            throw new IllegalArgumentException("Appointment date must be after the current date");
        }
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        if (!validateTime(appointmentTime)) {
            throw new IllegalArgumentException("Invalid appointment time");
        }
        this.appointmentTime = appointmentTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (!validateStatus(status)) {
            throw new IllegalArgumentException("Invalid status");
        }
        this.status = status;
    }

    // Validation methods
    private boolean validatePatientID(int patientID) {
        return patientID > 0; // Assume a positive integer for a valid patient ID
    }

    private boolean validateDoctorID(int doctorID) {
        return doctorID > 0; // Assume a positive integer for a valid doctor ID
    }

    private boolean validateDate(LocalDate appointmentDate) {
        return appointmentDate.isAfter(LocalDate.now()); // Date must be after the current date
    }

    private boolean validateTime(LocalTime appointmentTime) {
        return appointmentTime != null; // Time must not be null
    }

    private boolean validateStatus(String status) {
        // Status should be "booked", "pending", or "completed"
        return status.equals("booked") || status.equals("pending") || status.equals("completed");
    }

    // Logic to check payment status and update appointment status
    public void updateStatusBasedOnPayment(boolean isPaymentMade) {
        if (isPaymentMade) {
            this.status = "booked";
        } else {
            this.status = "pending";
        }
    }

    public void completeAppointment(boolean isPaymentCompleted) {
        if (isPaymentCompleted) {
            this.status = "completed";
        } else {
            this.status = "pending";
        }
    }

//    public static void main(String[] args) {
//        // Example usage
//        LocalDate date = LocalDate.of(2024, 6, 15); // Appointment date must be after the current date
//        LocalTime time = LocalTime.of(10, 30);
//
//        Appointment appointment = new Appointment(1, 1, date, time, "pending");
//        System.out.println("Initial status: " + appointment.getStatus());
//
//        // Update status based on payment
//        appointment.updateStatusBasedOnPayment(true);
//        System.out.println("Status after payment: " + appointment.getStatus());
//
//        // Complete appointment
//        appointment.completeAppointment(true);
//        System.out.println("Status after completing appointment: " + appointment.getStatus());
//    }
}
