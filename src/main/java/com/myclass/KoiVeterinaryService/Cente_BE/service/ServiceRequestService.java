package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;

import java.util.List;

public interface ServiceRequestService {
    CreateServiceRequestDTO createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO);
    ServiceRequestDTO markServiceRequestAsCompleted(Integer serviceRequestId);

    ServiceRequestDTO markServiceRequestAsCANCELLED(Integer serviceRequestId);

    ServiceRequestDTO markServiceRequestAsInProgress(Integer serviceRequestId);

    List<ServiceRequestDTO> getAllServiceRequest();

    List<ServiceRequestDTO> getServiceRequestByCustomerId(int customerId);

    List<ServiceRequestDTO> getServiceRequestByVeterinarianId(int veterinarianId);

    List<ServiceRequestDTO> getServiceRequestByStatus(String status);

    List<ServiceRequestDTO> getServiceRequestByMyInfor();
}
