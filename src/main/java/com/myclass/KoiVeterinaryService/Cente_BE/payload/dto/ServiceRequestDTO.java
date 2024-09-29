package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceRequestDTO {
    private int customerId; // ID của khách hàng (Account)

    private int veterinarianId; // ID của bác sĩ thú y (Account)

    private int serviceId; // ID của dịch vụ (ServiceKoi)

    private LocalDateTime appointmentTime; // Ngày và giờ hẹn

    private LocalDateTime endAppointmentTime; // Ngày và giờ kết thúc (tùy chọn)

    private Double total; // Tổng chi phí

    private EStatus status; // Trạng thái của yêu cầu
}
