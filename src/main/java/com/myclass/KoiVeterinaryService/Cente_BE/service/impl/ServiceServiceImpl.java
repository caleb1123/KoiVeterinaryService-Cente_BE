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
    public ServiceDTO updateService(UpdateServiceRequest serviceDTO,String serviceName) {
        Optional<ServiceKoi> existingServiceOpt = serviceRepository.findByServiceName(serviceName);
        if (existingServiceOpt.isPresent()) {
            ServiceKoi existingService = existingServiceOpt.get();
            existingService.setPrice(serviceDTO.getPrice());
            existingService.setDescription(serviceDTO.getDescription());
            existingService.setServiceType(serviceDTO.getServiceType());
            existingService.setActive(serviceDTO.isActive());

            existingService = serviceRepository.save(existingService);
            return modelMapper.map(existingService, ServiceDTO.class);
        } else {
            throw new AppException(ErrorCode.SERVICE_NOT_FOUND);
        }
    }

    @Override
    public boolean deleteService(String serviceName) {
        ServiceKoi service= new ServiceKoi();
        if (serviceRepository.existsByServiceName(serviceName)) {
            service = serviceRepository.findByServiceName(serviceName).get();
            serviceRepository.delete(service);
            return true;
        }
        throw new AppException(ErrorCode.SERVICE_NOT_FOUND); // Không tìm thấy dịch vụ để xóa
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
}
