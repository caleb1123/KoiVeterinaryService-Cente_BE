package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateServiceRequest;

import java.util.List;

public interface ServiceService {
    ServiceDTO createService(ServiceDTO serviceDTO);
    ServiceDTO updateService(UpdateServiceRequest serviceDTO,int serviceName);
    boolean deleteService(int serviceName);
    ServiceDTO findById(String serviceName);
    List<ServiceDTO> findAll();

    List<ServiceDTO> findServiceByType(String serviceType);

    List<ServiceDTO> findServiceByPrice(double price);

    List<ServiceDTO> findServiceByActive(boolean active);

    List<ServiceDTO> findServiceByServiceName(String serviceName);
}
