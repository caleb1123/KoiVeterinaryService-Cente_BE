package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackDTO {
    private int ratingId;

    private int customerId; // Reference to the customer account

    private int billId; // Reference to the bill

    private int ratingValue; // The rating score

    private String comment; // The feedback comment

    private LocalDate ratingDate; // Date when the rating was given

    private String feedbackType;
}
