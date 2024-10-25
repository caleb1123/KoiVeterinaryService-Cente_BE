package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceKoi;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateServiceRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceServiceImpl implements ServiceService {
    @Autowired
    private ServiceKoiRepository serviceRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ServiceDTO createService(ServiceDTO serviceDTO) {
        // Kiểm tra xem dịch vụ đã tồn tại chưa nếu cần
        if (serviceRepository.existsByServiceName(serviceDTO.getServiceName())) {
            throw new AppException(ErrorCode.SERVICE_EXISTED);
        }

        ServiceKoi service = modelMapper.map(serviceDTO, ServiceKoi.class);
        serviceRepository.save(service);
        return modelMapper.map(service, ServiceDTO.class);
    }

    @Override
    public ServiceDTO updateService(UpdateServiceRequest serviceDTO,int serviceName) {
        Optional<ServiceKoi> existingServiceOpt = serviceRepository.findById(serviceName);
        if (existingServiceOpt.isPresent()) {
            ServiceKoi existingService = existingServiceOpt.get();
            existingService.setPrice(serviceDTO.getPrice());
            if(serviceDTO.getDescription() != null){
                existingService.setDescription(serviceDTO.getDescription());
            }
            if(serviceDTO.getServiceType() != null){
                existingService.setServiceType(serviceDTO.getServiceType());
            }
            existingService.setActive(serviceDTO.isActive());

            existingService = serviceRepository.save(existingService);
            return modelMapper.map(existingService, ServiceDTO.class);
        } else {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
    }

    @Override
    public boolean deleteService(int serviceName) {
        ServiceKoi service= new ServiceKoi();
        service = serviceRepository.getReferenceById(serviceName);
        if (service != null) {
            serviceRepository.delete(service);
            return true;
        } else {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
    }

    @Override
    public ServiceDTO findById(String serviceName) {
        Optional<ServiceKoi> serviceOpt = serviceRepository.findByServiceName(serviceName);
        return serviceOpt.map(service -> modelMapper.map(service, ServiceDTO.class))
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND)); // Không tìm thấy dịch vụ
    }

    @Override
    public List<ServiceDTO> findAll() {
        List<ServiceKoi> services = serviceRepository.findAll();
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> findServiceByType(String serviceType) {
        List<ServiceKoi> services = serviceRepository.findServiceKoiByServiceType(serviceType);
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> findServiceByPrice(double price) {
        List<ServiceKoi> services = serviceRepository.findServiceKoiByPrice(price);
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> findServiceByActive(boolean active) {
        List<ServiceKoi> services = serviceRepository.findServiceKoiByIsActive(active);
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> findServiceByServiceName(String serviceName) {
        List<ServiceKoi> services = serviceRepository.findServiceKoiByServiceName(serviceName);
        return services.stream()
                .map(service -> modelMapper.map(service, ServiceDTO.class))
                .collect(Collectors.toList());
    }
}
