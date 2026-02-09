package com.crescent.hms.appointment.service;

import com.crescent.hms.appointment.model.Appointment;
import com.crescent.hms.appointment.model.VitalSigns;
import com.crescent.hms.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Transactional
    public Appointment bookAppointment(Long patientId, Long doctorId) {
        // 1. Get the last token for this doctor today
        Integer lastToken = appointmentRepository.findMaxTokenNumberByDoctorToday(doctorId);

        // 2. Calculate new token (Start from 1 if it's the first)
        Integer newToken = lastToken + 1;

        // 3. Create and Save Appointment
        Appointment appointment = new Appointment();
        appointment.setPatientId(patientId);
        appointment.setDoctorId(doctorId);
        appointment.setTokenNumber(newToken);
        appointment.setStatus(Appointment.Status.WAITING);

        return appointmentRepository.save(appointment);
    }

    public Appointment getAppointmentById(Long id) {
        return appointmentRepository.findById(id).orElse(null);
    }

    // Vitals Logic
    @Transactional
    public void addVitals(Long appointmentId, VitalSigns vitals) {
        Appointment appointment = appointmentRepository.findById(appointmentId).orElseThrow();

        vitals.setAppointment(appointment);
        appointment.setVitalSigns(vitals);

        // Update status
        appointment.setStatus(Appointment.Status.VITALS_DONE);

        appointmentRepository.save(appointment);
    }
}