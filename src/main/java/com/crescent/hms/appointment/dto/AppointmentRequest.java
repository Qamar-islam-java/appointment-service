package com.crescent.hms.appointment.dto;

import lombok.Data;

@Data
// Helper class for receiving JSON
public class AppointmentRequest {
    private Long patientId;
    private Long doctorId;

//    // Getters and Setters
//    public Long getPatientId() { return patientId; }
//    public void setPatientId(Long patientId) { this.patientId = patientId; }
//    public Long getDoctorId() { return doctorId; }
//    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}