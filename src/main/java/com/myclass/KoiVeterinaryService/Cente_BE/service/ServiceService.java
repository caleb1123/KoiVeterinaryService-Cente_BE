package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateServiceRequest;

import java.util.List;

public interface ServiceService {
    ServiceDTO createService(ServiceDTO serviceDTO);
    ServiceDTO updateService(UpdateServiceRequest serviceDTO,String serviceName);
    boolean deleteService(String serviceName);
    ServiceDTO findById(String serviceName);
    List<ServiceDTO> findAll();
}