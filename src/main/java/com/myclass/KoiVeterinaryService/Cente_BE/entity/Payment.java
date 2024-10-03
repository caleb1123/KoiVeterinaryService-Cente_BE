package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Payment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int paymentId;

        @Column
        private LocalDate paymentDate;

        @Column
        private String paymentStatus;

        @Column
        private double amount;
        @Column
        private int paymentCode;

        @ManyToOne
        @JoinColumn(name = "requestId")
        private ServiceRequest serviceRequest;

        @ManyToOne
        @JoinColumn(name = "customerId")
        private Account account;
}
