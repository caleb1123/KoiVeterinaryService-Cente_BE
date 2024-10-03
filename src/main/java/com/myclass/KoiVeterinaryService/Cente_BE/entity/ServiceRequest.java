package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "ServiceRequest")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    private Account veterinarian;


    @Column(nullable = false)
    private LocalDate appointmentTime;

    @Column
    private LocalDateTime completedTime;

    @ManyToOne
    @JoinColumn(name = "shift_id", nullable = false)
    private Shift shift;


    // Sử dụng EStatus enum cho trường status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatus status;

    @OneToMany(mappedBy = "serviceRequest")
    private List<Bill> bills;

    @OneToMany(mappedBy = "serviceRequest")
    private List<Payment> payments;
}
