package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.Data;


import lombok.Builder;

@Data
@Builder
public class PaymentRequest2 {
    private int requestId;
    private String code;
    private String message;
    private String paymentUrl;
}

