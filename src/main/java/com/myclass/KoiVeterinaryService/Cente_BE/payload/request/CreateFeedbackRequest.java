package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateFeedbackRequest {

    private int veterinarianId; // Reference to the veterinarian account

    private int serviceId; // Reference to the service provided

    private int ratingValue; // The rating score

    private String comment; // The feedback comment

    private String ratingDate; // Date when the rating was given

    private String feedbackType;
}
