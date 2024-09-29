package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "Rating")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Account customer;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private Account veterinarian;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private ServiceKoi service;

    @Column(nullable = false)
    private int ratingValue;

    @Column
    private String comment;

    @Column(nullable = false)
    private LocalDate ratingDate;
}
