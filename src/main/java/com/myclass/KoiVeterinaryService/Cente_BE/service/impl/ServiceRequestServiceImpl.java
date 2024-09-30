package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.*;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceRequestRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ShiftRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
    @Autowired
    ShiftRepository shiftRepository;
    @Override
    public CreateServiceRequestDTO createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO) {
        Account customer = accountRepository.findAccountByAccountIdAndRole(1, serviceRequestDTO.getCustomerId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));


        Shift shift = shiftRepository.findById(serviceRequestDTO.getShiftId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIFT_NOT_EXISTED));

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCustomer(customer);
        serviceRequest.setAppointmentTime(serviceRequestDTO.getAppointmentTime());
        serviceRequest.setStatus(EStatus.PENDING);
        serviceRequest.setShift(shift);

        if (serviceRequestDTO.getVeterinarianId() == 0) {
            // Find the list of available veterinarians for the given appointment time and shift
            List<Object[]> availableVeterinarians = accountRepository.findAvailableVeterinarians(serviceRequestDTO.getAppointmentTime(), serviceRequestDTO.getShiftId());

            // Check if there are any available veterinarians
            if (availableVeterinarians.isEmpty()) {
                throw new AppException(ErrorCode.VETERINARIAN_NOT_AVAILABLE);
            }

            // Get the first available veterinarian (you can customize this logic if needed)
            Object[] selectedVet = availableVeterinarians.get(0);  // Assuming the first element is the veterinarian ID
            Integer selectedVetId = (Integer) selectedVet[0];  // Assuming vet[0] is the veterinarian ID

            // Fetch the Account of the selected veterinarian
            Account veterinarian = accountRepository.findById(selectedVetId)
                    .orElseThrow(() -> new AppException(ErrorCode.VETERINARIAN_NOT_EXISTED));

            // Assign the veterinarian to the service request
            serviceRequest.setVeterinarian(veterinarian);
        } else {
            // If veterinarianId != 0, validate if the provided veterinarian is available
            List<Object[]> veterinarians = accountRepository.findAvailableVeterinarians(serviceRequestDTO.getAppointmentTime(), serviceRequestDTO.getShiftId());

            boolean vetExists = veterinarians.stream()
                    .anyMatch(vet -> vet[0].equals(serviceRequestDTO.getVeterinarianId()));

            if (!vetExists) {
                throw new AppException(ErrorCode.VETERINARIAN_NOT_AVAILABLE);
            }

            // Set the provided veterinarian
            Account veterinarian = accountRepository.findById(serviceRequestDTO.getVeterinarianId())
                    .orElseThrow(() -> new AppException(ErrorCode.VETERINARIAN_NOT_EXISTED));

            serviceRequest.setVeterinarian(veterinarian);
        }

        serviceRequestRepository.save(serviceRequest);
        serviceRequestDTO.setVeterinarianId(serviceRequest.getVeterinarian().getAccountId());
        return serviceRequestDTO;
    }

    @Override
    public ServiceRequestDTO markServiceRequestAsCompleted(Integer serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_EXISTED));
        if(serviceRequest.getStatus() != EStatus.PENDING){
            throw new AppException(ErrorCode.SERVICE_REQUEST_NOT_PENDING);
        }
        serviceRequest.setCompletedTime(LocalDateTime.now());
        serviceRequest.setStatus(EStatus.COMPLETED);
        serviceRequestRepository.save(serviceRequest);
        ServiceRequestDTO serviceRequestDTO = modelMapper.map(serviceRequest, ServiceRequestDTO.class);
        serviceRequestDTO.setCustomerId(serviceRequest.getCustomer().getAccountId());
        serviceRequestDTO.setVeterinarianId(serviceRequest.getVeterinarian().getAccountId());
        serviceRequestDTO.setShiftId(serviceRequest.getShift().getShiftId());
        return serviceRequestDTO;
    }

    @Override
    public ServiceRequestDTO markServiceRequestAsCANCELLED(Integer serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_EXISTED));
        if(serviceRequest.getStatus() == EStatus.COMPLETED){
            throw new AppException(ErrorCode.SERVICE_REQUEST_COMPLETED);
        }
        serviceRequest.setStatus(EStatus.CANCELLED);
        serviceRequestRepository.save(serviceRequest);
        ServiceRequestDTO serviceRequestDTO = modelMapper.map(serviceRequest, ServiceRequestDTO.class);
        serviceRequestDTO.setCustomerId(serviceRequest.getCustomer().getAccountId());
        serviceRequestDTO.setVeterinarianId(serviceRequest.getVeterinarian().getAccountId());
        serviceRequestDTO.setShiftId(serviceRequest.getShift().getShiftId());
        return serviceRequestDTO;
    }

}
