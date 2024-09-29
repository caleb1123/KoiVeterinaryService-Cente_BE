package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.EStatus;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceKoi;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceRequestRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ServiceRequestServiceImpl implements ServiceRequestService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ServiceKoiRepository serviceKoiRepository;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Override
    public ServiceRequestDTO createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO) {
        Account customer = accountRepository.findAccountByAccountIdAndRole(1,serviceRequestDTO.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        ServiceKoi serviceKoi = serviceKoiRepository.findById(serviceRequestDTO.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));
        if(serviceRequestDTO.getVeterinarianId() != 0){
            Account veterinarian = accountRepository.findAccountByAccountIdAndRole(3,serviceRequestDTO.getVeterinarianId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            ServiceRequest serviceRequest = new ServiceRequest();
            serviceRequest.setCustomer(customer);
            serviceRequest.setVeterinarian(veterinarian);
            serviceRequest.setService(serviceKoi);
            serviceRequest.setAppointmentTime(serviceRequestDTO.getAppointmentTime());
            serviceRequest.setStatus(EStatus.PENDING);
            serviceRequestRepository.save(serviceRequest);
            return modelMapper.map(serviceRequest, ServiceRequestDTO.class);
        }
        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCustomer(customer);
        serviceRequest.setService(serviceKoi);
        serviceRequest.setAppointmentTime(serviceRequestDTO.getAppointmentTime());
        serviceRequest.setStatus(EStatus.PENDING);
        serviceRequestRepository.save(serviceRequest);
        return modelMapper.map(serviceRequest, ServiceRequestDTO.class);
    }
}
