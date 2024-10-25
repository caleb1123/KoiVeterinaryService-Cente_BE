package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.*;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ShiftDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.CreateServiceResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.HistoryResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceRequestRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ShiftRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceRequestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    public CreateServiceResponse createVetAppointmentService(CreateServiceRequestDTO serviceRequestDTO) {
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

        CreateServiceResponse createServiceResponse = new CreateServiceResponse();
        createServiceResponse.setAppointmentTime(serviceRequest.getAppointmentTime());
        createServiceResponse.setCustomerId(modelMapper.map(serviceRequest.getCustomer(), AccountDTO.class));
        createServiceResponse.setShiftId(serviceRequest.getShift().getShiftId());
        createServiceResponse.setVeterinarianId(modelMapper.map(serviceRequest.getVeterinarian(), AccountDTO.class));
        return createServiceResponse;
    }

    @Override
    public ServiceRequestDTO markServiceRequestAsCompleted(Integer serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_EXISTED));
        if(serviceRequest.getStatus() != EStatus.IN_PROGRESS){
            throw new AppException(ErrorCode.SERVICE_REQUEST_NOT_IN_PROGRESS);
        }
        serviceRequest.setCompletedTime(LocalDateTime.now());
        serviceRequest.setStatus(EStatus.COMPLETED);
        serviceRequestRepository.save(serviceRequest);
        ServiceRequestDTO serviceRequestDTO = modelMapper.map(serviceRequest, ServiceRequestDTO.class);
        serviceRequestDTO.setCustomerId(modelMapper.map(serviceRequest.getCustomer(), AccountDTO.class));
        serviceRequestDTO.setVeterinarianId(modelMapper.map(serviceRequest.getVeterinarian(), AccountDTO.class));
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
        serviceRequestDTO.setCustomerId(modelMapper.map(serviceRequest.getCustomer(), AccountDTO.class));
        serviceRequestDTO.setVeterinarianId(modelMapper.map(serviceRequest.getVeterinarian(), AccountDTO.class));
        serviceRequestDTO.setShiftId(serviceRequest.getShift().getShiftId());
        return serviceRequestDTO;
    }

    @Override
    public ServiceRequestDTO markServiceRequestAsInProgress(Integer serviceRequestId) {
        ServiceRequest serviceRequest = serviceRequestRepository.findById(serviceRequestId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_REQUEST_NOT_EXISTED));
        if(serviceRequest.getStatus() != EStatus.PENDING){
            throw new AppException(ErrorCode.SERVICE_REQUEST_NOT_PENDING);
        }
        serviceRequest.setStatus(EStatus.IN_PROGRESS);
        serviceRequestRepository.save(serviceRequest);
        ServiceRequestDTO serviceRequestDTO = modelMapper.map(serviceRequest, ServiceRequestDTO.class);

        return serviceRequestDTO;
    }

    @Override
    public List<ServiceRequestDTO> getAllServiceRequest() {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findAll();
        return serviceRequests.stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestDTO.class)).toList();
    }

    @Override
    public List<HistoryResponse> getServiceRequestByCustomerId(int customerId) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findServiceRequestsByCustomer_AccountId(customerId);

        List<HistoryResponse> historyResponses = serviceRequests.stream().map(serviceRequest -> {
            ShiftDTO shift = new ShiftDTO();
            shift.setId(serviceRequest.getShift().getShiftId());
            shift.setName(String.valueOf(serviceRequest.getShift().getShiftName()));
            return HistoryResponse.builder()
                    .requestId(serviceRequest.getRequestId())
                    .customer(modelMapper.map(serviceRequest.getCustomer(), AccountDTO.class)) // Using ModelMapper to map to AccountDTO
                    .veterinarianId(serviceRequest.getVeterinarian() != null
                            ? modelMapper.map(serviceRequest.getVeterinarian(), AccountDTO.class)
                            : null) // Optional veterinarian, mapped using ModelMapper
                    .shift(shift) // Using ModelMapper to map to ShiftDTO
                    .appointmentTime(serviceRequest.getAppointmentTime())
                    .completedTime(serviceRequest.getCompletedTime())
                    .status(serviceRequest.getStatus().toString()) // Assuming status is an ENUM
                    .build();
        }).collect(Collectors.toList());

        return historyResponses;
    }

    @Override
    public List<ServiceRequestDTO> getServiceRequestByVeterinarianId(int veterinarianId) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findServiceRequestsByVeterinarian_AccountId(veterinarianId);
        return serviceRequests.stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestDTO.class)).toList();
    }

    @Override
    public List<ServiceRequestDTO> getServiceRequestByStatus(String status) {
        List<ServiceRequest> serviceRequests = serviceRequestRepository.findServiceRequestsByStatus(EStatus.valueOf(status));
        return serviceRequests.stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestDTO.class)).toList();
    }

    @Override
    public List<ServiceRequestDTO> getServiceRequestByMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        Account byUserName = accountRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<ServiceRequest> serviceRequests = serviceRequestRepository.findServiceRequestsByCustomer_AccountId(byUserName.getAccountId());
        return serviceRequests.stream().map(serviceRequest -> modelMapper.map(serviceRequest, ServiceRequestDTO.class)).toList();
    }

}
