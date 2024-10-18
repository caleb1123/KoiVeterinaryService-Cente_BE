package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "feedback")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int ratingId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false) // Use the correct column name from your DB
    private Account customer;


    @ManyToOne
    @JoinColumn(name = "request_id", nullable = false)
    private ServiceRequest request;

    @Column(nullable = false)
    private int ratingValue;

    @Column
    private String comment;

    @Column(nullable = false)
    private LocalDate ratingDate;

    @Enumerated(EnumType.STRING) // Assuming FeedbackType is an enum
    @Column(nullable = false)
    private FeedbackType feedbackType;
}
