package com.crescent.hms.appointment.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class VitalSigns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double weight;   // kg
    private int height;      // cm
    private int systolicBP;
    private int diastolicBP;
    private double temperature;
    private String pulse;

    // OneToOne link to Appointment
    @OneToOne
    @JoinColumn(name = "appointment_id")
    private Appointment appointment;
}