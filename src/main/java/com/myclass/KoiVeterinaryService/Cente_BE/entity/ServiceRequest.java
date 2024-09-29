package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceKoi service;

    @Column(nullable = false)
    private LocalDateTime appointmentTime;  // Ngày và giờ hẹn

    @Column
    private LocalDateTime endAppointmentTime;  // Ngày và giờ kết thúc (tùy chọn)


    // Sử dụng EStatus enum cho trường status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EStatus status;
}
