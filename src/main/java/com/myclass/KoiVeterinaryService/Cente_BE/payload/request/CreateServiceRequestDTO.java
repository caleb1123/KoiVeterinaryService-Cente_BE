package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateServiceRequestDTO {
    private int customerId; // ID của khách hàng (Account)

    private int veterinarianId; // ID của bác sĩ thú y (Account)

    private int serviceId; // ID của dịch vụ (ServiceKoi)

    private LocalDateTime appointmentTime; // Ngày và giờ hẹn


}
