package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.config.VnPayConfig;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Payment;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentRequest2;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.PaymentResquest;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.BillResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.dto.PaymentDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.PaymentResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.BillRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.PaymentRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceRequestRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.PaymentService;
import com.myclass.KoiVeterinaryService.Cente_BE.vnpay.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private ServiceRequestRepository serviceRequestRepository;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VnPayConfig VNPayConfig;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PaymentResquest createVnPayPayment(PaymentRequest2 paymentRequest2, HttpServletRequest request) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(paymentRequest2.getRequestId())
                .orElseThrow(() -> new AppException(ErrorCode.REQUEST_NOT_FOUND));

        Account account = accountRepository.findById(serviceRequest.getCustomer().getAccountId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Object result = billRepository.findTotalAmount(paymentRequest2.getRequestId());
        // Ensure the result is not null and is an array of Object
        if (result == null || !(result instanceof Object[])) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        // Cast the result to Object array
        Object[] resultArray = (Object[]) result;

        // Check if the result array has the expected length
        if (resultArray.length < 2) {
            throw new AppException(ErrorCode.BILL_NOT_FOUND);
        }

        // Create BillResponse object and populate it
        BillResponse billResponse = new BillResponse();

        // Cast and set the values
        billResponse.setRequestId(((Number) resultArray[0]).intValue()); // Cast to Number and get int
        billResponse.setTotal_amount(((Number) resultArray[1]).doubleValue()); // Cast to Number and get double

        int requestId = paymentRequest2.getRequestId();
        String username = account.getUserName();
        long amount = (long) billResponse.getTotal_amount()*100;
        String bankCode = paymentRequest2.getBankCode();
        String transactionId = paymentRequest2.getTransactionId();
        Map<String, String> vnpParamsMap;
        if (transactionId != null) {
            int id = Integer.parseInt(transactionId);
            vnpParamsMap = VNPayConfig.getVNPayConfig(requestId, username, id);
        } else {
            vnpParamsMap = VNPayConfig.getVNPayConfig(requestId, username, 0);
        }
        vnpParamsMap.put("vnp_Amount", String.valueOf(amount));
        if (bankCode != null && !bankCode.isEmpty()) {
            vnpParamsMap.put("vnp_BankCode", bankCode);
        }
        vnpParamsMap.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));

        // Build query URL
        String queryUrl = VNPayUtil.getPaymentURL(vnpParamsMap, true);
        String hashData = VNPayUtil.getPaymentURL(vnpParamsMap, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(VNPayConfig.getSecretKey(), hashData);
        queryUrl += "&vnp_SecureHash=" + vnpSecureHash;
        String paymentUrl = VNPayConfig.getVnpPayUrl() + "?" + queryUrl;

        Payment payment = new Payment();
        payment.setAmount(billResponse.getTotal_amount());
        payment.setPaymentStatus("PENDING");
        payment.setPaymentDate(LocalDate.now());
        payment.setServiceRequest(serviceRequest);
        payment.setPaymentCode(Integer.parseInt(vnpParamsMap.get("vnp_TxnRef")));
        payment.setAccount(account);
        paymentRepository.save(payment);
        return PaymentResquest.builder()
                .code("ok")
                .message("success")
                .paymentUrl(paymentUrl).build();



    }

    @Override
    public PaymentResponse handleCallback(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        String paymentCode = request.getParameter("vnp_TxnRef");
        Payment payment = paymentRepository.getPaymentByCode(paymentCode);
        if (status.equals("00")) {
            payment.setPaymentStatus("SUCCESSFUL");
            payment.setPaymentDate(LocalDate.now());
            paymentRepository.save(payment);
            return new PaymentResponse(status, "SUCCESSFUL", modelMapper.map(payment, PaymentDTO.class));
        }
        else {
            payment.setPaymentStatus("FAILED");
            paymentRepository.save(payment);
            return new PaymentResponse(status, "FAILED", modelMapper.map(payment, PaymentDTO.class));
        }
    }

    @Override
    public List<PaymentDTO> findAll() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
    }

    @Override
    public PaymentDTO findByPaymentCode(String code) {
        Payment payment = paymentRepository.findPaymentByPaymentCode(Integer.parseInt(code));
        if (payment == null) {
            throw new AppException(ErrorCode.PAYMENT_NOT_FOUND);
        }
        return modelMapper.map(payment, PaymentDTO.class);
    }

    @Override
    public List<PaymentDTO> findByCustomer(int accountId) {
         List<Payment> payments = paymentRepository.findPaymentByAccount_AccountId(accountId);
         return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
    }

    @Override
    public List<PaymentDTO> findByRequest(int requestId) {
        List<Payment> payments = paymentRepository.findPaymentByServiceRequest_RequestId(requestId);
        return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
    }

    @Override
    public List<PaymentDTO> findByStatus(String status) {
        List<Payment> payments = paymentRepository.findPaymentByPaymentStatus(status);
        return payments.stream().map(payment -> modelMapper.map(payment, PaymentDTO.class)).toList();
    }
}
