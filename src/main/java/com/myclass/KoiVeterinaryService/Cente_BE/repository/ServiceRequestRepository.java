package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Integer> {
    @Query(value = "SELECT * FROM service_request where appointment_time = '2024-09-29 15:50:28.104000'", nativeQuery = true)
    ServiceRequest findRequestByDate(String date);

    List<ServiceRequest> findServiceRequestsByCustomer_AccountId(int customerId);

    List<ServiceRequest> findServiceRequestsByVeterinarian_AccountId(int veterinarianId);

    List<ServiceRequest> findServiceRequestsByStatus(EStatus status);
}
