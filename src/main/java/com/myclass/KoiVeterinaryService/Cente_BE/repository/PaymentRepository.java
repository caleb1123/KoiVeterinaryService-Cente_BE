package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    @Query(value = "SELECT * FROM Payment WHERE payment_code = :paymentCode", nativeQuery = true)
    Payment getPaymentByCode(String paymentCode);

    Payment findPaymentByPaymentCode(int paymentCode);
    List<Payment> findPaymentByAccount_AccountId(int accountId);

    List<Payment> findPaymentByServiceRequest_RequestId(int requestId);

    List<Payment> findPaymentByPaymentStatus(String status);
}
