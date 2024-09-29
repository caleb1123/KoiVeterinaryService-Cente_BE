package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;

public interface ServiceRequestService {
    CreateServiceRequestDTO createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO);

}
