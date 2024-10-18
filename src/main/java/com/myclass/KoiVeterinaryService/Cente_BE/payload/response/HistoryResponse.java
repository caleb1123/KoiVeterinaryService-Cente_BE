package com.myclass.KoiVeterinaryService.Cente_BE.payload.response;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ShiftDTO;
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
public class HistoryResponse {

    private int requestId; // The ID of the service request

    private AccountDTO customer; // The customer ID
    private AccountDTO veterinarianId; // The veterinarian ID (optional)
    private ShiftDTO shift;
    private LocalDate appointmentTime; // The appointment time
    private LocalDateTime completedTime; // The completed time (optional)

    private String status; // The status of the request (ENUM as String)

    // You can add more fields if necessary, such as service name, veterinarian name, etc.
}
