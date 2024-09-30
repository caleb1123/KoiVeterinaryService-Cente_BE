package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateServiceRequestDTO {
    private int customerId; // ID của khách hàng (Account)

    private int veterinarianId; // ID của bác sĩ thú y (Account)

    private int shiftId; // ID của ca làm việc (Shift)

    private LocalDate appointmentTime; // Ngày và giờ hẹn


}
