package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDTO {

    private int requestId; // The ID of the service request

    private AccountDTO customerId; // The customer ID
    private AccountDTO veterinarianId; // The veterinarian ID (optional)
    private int shiftId; // The shift ID

    private LocalDate appointmentTime; // The appointment time
    private LocalDateTime completedTime; // The completed time (optional)

    private String status; // The status of the request (ENUM as String)

    // You can add more fields if necessary, such as service name, veterinarian name, etc.
}
