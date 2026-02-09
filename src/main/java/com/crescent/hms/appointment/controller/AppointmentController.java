package com.crescent.hms.appointment.controller;

import com.crescent.hms.appointment.model.Appointment;
import com.crescent.hms.appointment.model.VitalSigns;
import com.crescent.hms.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // Receptionist: Book Appointment
    // Input: {"patientId": 1, "doctorId": 1}
    @PostMapping("/book")
    public ResponseEntity<?> bookAppointment(@RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.bookAppointment(request.getPatientId(), request.getDoctorId());
        return ResponseEntity.ok(appointment);
    }

    // Nurse: Submit Vitals
    @PostMapping("/{id}/vitals")
    public ResponseEntity<?> submitVitals(@PathVariable Long id, @RequestBody VitalSigns vitals) {
        appointmentService.addVitals(id, vitals);
        return ResponseEntity.ok("Vitals saved successfully. Patient ready for doctor.");
    }

    // Dashboard/Doctor: Check specific appointment
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    // Helper class for receiving JSON
    public static class AppointmentRequest {
        private Long patientId;
        private Long doctorId;

        // Getters and Setters
        public Long getPatientId() { return patientId; }
        public void setPatientId(Long patientId) { this.patientId = patientId; }
        public Long getDoctorId() { return doctorId; }
        public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
    }
}