package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.CreateServiceResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.HistoryResponse;

import java.util.List;

public interface ServiceRequestService {
    CreateServiceResponse createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO);
    ServiceRequestDTO markServiceRequestAsCompleted(Integer serviceRequestId);

    ServiceRequestDTO markServiceRequestAsCANCELLED(Integer serviceRequestId);

    ServiceRequestDTO markServiceRequestAsInProgress(Integer serviceRequestId);

    List<ServiceRequestDTO> getAllServiceRequest();

    List<HistoryResponse> getServiceRequestByCustomerId(int customerId);

    List<ServiceRequestDTO> getServiceRequestByVeterinarianId(int veterinarianId);

    List<ServiceRequestDTO> getServiceRequestByStatus(String status);

    List<ServiceRequestDTO> getServiceRequestByMyInfor();
}
