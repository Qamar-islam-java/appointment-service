package com.crescent.hms.appointment.repository;

import com.crescent.hms.appointment.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // Find all appointments for a doctor today
    List<Appointment> findByDoctorIdAndCreatedDateBetween(Long doctorId, java.time.LocalDateTime start, java.time.LocalDateTime end);

    // Find the latest token number for a specific doctor today
    @Query("SELECT COALESCE(MAX(a.tokenNumber), 0) FROM Appointment a WHERE a.doctorId = :doctorId AND FUNCTION('DATE', a.createdDate) = CURRENT_DATE")
    Integer findMaxTokenNumberByDoctorToday(@Param("doctorId") Long doctorId);

    // Find by Token and Doctor (For Dashboard)
    Optional<Appointment> findByDoctorIdAndTokenNumber(Long doctorId, Integer tokenNumber);
}