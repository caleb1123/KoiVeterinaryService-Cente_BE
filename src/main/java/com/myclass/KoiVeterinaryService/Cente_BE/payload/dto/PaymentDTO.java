package com.myclass.KoiVeterinaryService.Cente_BE.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private int paymentId;
    private LocalDate paymentDate;
    private String paymentStatus;
    private double amount;
    private int paymentCode;
    private int requestId;
    private int customerId;
}
