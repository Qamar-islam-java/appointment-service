package com.crescent.hms.appointment.service;

import com.crescent.hms.appointment.enums.Status;
import com.crescent.hms.appointment.model.Appointment;
import com.crescent.hms.appointment.model.VitalSigns;
import com.crescent.hms.appointment.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        appointment.setStatus(Status.WAITING);

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
        appointment.setStatus(Status.VITALS_DONE);

        appointmentRepository.save(appointment);
    }
    //-------------------------------------------------------------//
    // ... existing bookAppointment, addVitals methods ...

    // 1. Get Doctor's Queue (Patients ready for consultation)
    public List<Appointment> getDoctorQueue(Long doctorId) {
        // We only show patients where Vitals are done (Status: VITALS_DONE)
        List<Appointment> appointmentList =    appointmentRepository.findByDoctorIdAndStatus(doctorId, Status.VITALS_DONE);
     //   System.out.println("-----------getDoctorQueue appointmentList.size()" + appointmentList);
        return appointmentList;
    }

    // 2. Start Consultation
    @Transactional
    public void startConsultation(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        if (appointment.getStatus() != Status.VITALS_DONE) {
            throw new RuntimeException("Patient vitals not completed yet!");
        }

        appointment.setStatus(Status.IN_PROGRESS);
        appointmentRepository.save(appointment);
    }

    // 3. Complete Consultation
    @Transactional
    public void completeConsultation(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        appointment.setStatus(Status.COMPLETED);
        appointmentRepository.save(appointment);
    }
}