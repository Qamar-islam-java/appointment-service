package com.crescent.hms.appointment.model;

import com.crescent.hms.appointment.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Data
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long doctorId;

    private Integer tokenNumber;

    @Enumerated(EnumType.STRING)
    private Status status;

    @CreationTimestamp
    private LocalDateTime createdDate;

    @ToString.Exclude
    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private VitalSigns vitalSigns;
}
//@Entity
//@Data
//@ToString(exclude = "vitalSigns")
//public class Appointment {
//    public enum Status {
//        WAITING,         // Receptionist booked it
//        VITALS_DONE,     // Nurse checked vitals
//        IN_PROGRESS,     // Doctor is seeing patient
//        COMPLETED        // Done
//    }
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // We store IDs. In a full system, we might use @ManyToOne relationships to other services if they shared a DB,
//    // but in Microservices, storing the ID is standard.
//    private Long patientId;
//    private Long doctorId;
//
//    private Integer tokenNumber; // e.g., 101
//
//    @Enumerated(EnumType.STRING)
//    private Status status;
//
//    @CreationTimestamp
//    private LocalDateTime createdDate;
//
//    // Relationship to Vitals (Optional initially)
//    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
//    private VitalSigns vitalSigns;
//}