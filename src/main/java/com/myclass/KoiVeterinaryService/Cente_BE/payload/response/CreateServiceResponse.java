package com.myclass.KoiVeterinaryService.Cente_BE.payload.response;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateServiceResponse {
    private AccountDTO customerId; // ID của khách hàng (Account)

    private AccountDTO veterinarianId; // ID của bác sĩ thú y (Account)

    private int shiftId; // ID của ca làm việc (Shift)

    private LocalDate appointmentTime; // Ngày và giờ hẹn


}
