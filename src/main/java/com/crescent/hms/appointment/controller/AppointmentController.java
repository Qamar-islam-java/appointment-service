package com.crescent.hms.appointment.controller;

import com.crescent.hms.appointment.dto.AppointmentRequest;
import com.crescent.hms.appointment.model.Appointment;
import com.crescent.hms.appointment.model.VitalSigns;
import com.crescent.hms.appointment.repository.AppointmentRepository;
import com.crescent.hms.appointment.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private  AppointmentRepository  appointmentRepository;

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

//    // Dashboard/Doctor: Check specific appointment
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getAppointment(@PathVariable Long id) {
//        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
//    }


    //-----------------DOCTOR DASHBOARD ENDPOINTS STARTS----------------//

    // 1. Get Queue
    // Usage: GET /api/appointment/doctor/1/queue
    @GetMapping("/doctor/{doctorId}/queue")
    public ResponseEntity<List<Appointment>> getDoctorQueue(@PathVariable Long doctorId) {
        List<Appointment> a = appointmentService.getDoctorQueue(doctorId);
        System.out.println("-------------"+a);
        return ResponseEntity.ok(a);
    }

    // 2. Start Consultation
    // Usage: POST /api/appointment/1/start
    @PostMapping("/{id}/start")
    public ResponseEntity<?> startConsultation(@PathVariable Long id) {
        appointmentService.startConsultation(id);
        return ResponseEntity.ok("Consultation Started");
    }

    // 3. Complete Consultation
    // Usage: POST /api/appointment/1/complete
    @PostMapping("/{id}/complete")
    public ResponseEntity<?> completeConsultation(@PathVariable Long id) {
        appointmentService.completeConsultation(id);
        return ResponseEntity.ok("Consultation Completed");
    }

    // 4. Get Full Details (Includes Vitals)
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentDetails(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }
    //-----------------DOCTOR DASHBOARD ENDPOINTS ENDS----------------//


    //-----------------HELPER FOR PHARMACY-----------------------------//
    @GetMapping("/search")
    public ResponseEntity<?> findByTokenAndDoctor(@RequestParam Integer token, @RequestParam Long doctorId) {
        Appointment appointment = appointmentRepository.findByDoctorIdAndTokenNumber(doctorId, token)
                .orElse(null);

        if (appointment == null) {
            return ResponseEntity.notFound().build();
        }

        // Return a simple Map so Pharmacy Service easily extracts the ID
        return ResponseEntity.ok(Map.of("id", appointment.getId(), "patientId", appointment.getPatientId()));
    }

}