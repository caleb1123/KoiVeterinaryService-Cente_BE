package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentRequest2;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentResquest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.PaymentResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.dto.PaymentDTO;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface PaymentService {
    PaymentResquest createVnPayPayment(PaymentRequest2 paymentRequest2, HttpServletRequest request);

    PaymentResponse handleCallback(HttpServletRequest request);

    List<PaymentDTO> findAll();

    PaymentDTO findByPaymentCode(String code);

    List<PaymentDTO> findByCustomer(int accountId);

    List<PaymentDTO> findByRequest(int requestId);

    List<PaymentDTO> findByStatus(String status);
}
