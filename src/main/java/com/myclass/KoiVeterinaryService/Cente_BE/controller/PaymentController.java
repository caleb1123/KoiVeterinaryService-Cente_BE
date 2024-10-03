package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentRequest2;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentResquest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.PaymentResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.dto.PaymentDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.service.PaymentService;
import com.myclass.KoiVeterinaryService.Cente_BE.vnpay.ResponseObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/payment")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseObject<PaymentResquest> pay(@RequestBody PaymentRequest2 request, HttpServletRequest httpServletRequest) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request, httpServletRequest));
    }

    @GetMapping("/return")
    public void payCallbackHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String url = "http://localhost:3001/PaymentSuccess";
        String urlFail = "http://localhost:3001/PaymentFailed";
        PaymentResponse payment = paymentService.handleCallback(request);
        if (payment.getCode().equals("00")) {
            response.sendRedirect(url);
        } else {
            response.sendRedirect(urlFail);
        }
    }

    @GetMapping("/payment/{code}")
    public ResponseObject<PaymentDTO> getPaymentByCode(@PathVariable String code) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.findByPaymentCode(code));
    }

    @GetMapping("/payments")
    public ResponseObject<List<PaymentDTO>> getAllPayment() {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.findAll());
    }

    @GetMapping("/payment/customer/{accountId}")
    public ResponseObject<List<PaymentDTO>> getPaymentByCustomer(@PathVariable int accountId) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.findByCustomer(accountId));
    }

    @GetMapping("/payment/request/{requestId}")
    public ResponseObject<List<PaymentDTO>> getPaymentByRequest(@PathVariable int requestId) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.findByRequest(requestId));
    }

    @GetMapping("/payment/status/{status}")
    public ResponseObject<List<PaymentDTO>> getPaymentByStatus(@PathVariable String status) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.findByStatus(status));
    }
}
