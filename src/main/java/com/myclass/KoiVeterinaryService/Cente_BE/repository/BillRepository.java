package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Integer> {
    @Query(value = "SELECT * FROM Bill b WHERE b.request_id = :requestId", nativeQuery = true)
    List<Bill> findByRequest(int requestId);

    @Query(value = "SELECT * FROM Bill b WHERE b.request_id = :requestId AND b.status = 1", nativeQuery = true)
    List<Bill> findRequestByActive(int requestId);

    @Query(value = "SELECT b.request_id, SUM(b.quantity * s.price) AS total_amount " +
            "FROM bill b " +
            "JOIN service s ON b.service_id = s.service_id " +
            "WHERE b.request_id = :requestId " +
            "GROUP BY b.request_id", nativeQuery = true)
    Object findTotalAmount(@Param("requestId") int requestId);

}
