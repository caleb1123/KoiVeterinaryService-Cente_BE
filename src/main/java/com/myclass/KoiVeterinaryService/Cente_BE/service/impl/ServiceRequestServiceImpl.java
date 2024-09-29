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

        ServiceKoi serviceKoi = serviceKoiRepository.findById(serviceRequestDTO.getServiceId())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_EXISTED));

        Shift shift = shiftRepository.findById(serviceRequestDTO.getShiftId())
                .orElseThrow(() -> new AppException(ErrorCode.SHIFT_NOT_EXISTED));

        ServiceRequest serviceRequest = new ServiceRequest();
        serviceRequest.setCustomer(customer);
        serviceRequest.setService(serviceKoi);
        serviceRequest.setAppointmentTime(serviceRequestDTO.getAppointmentTime());
        serviceRequest.setStatus(EStatus.PENDING);
        serviceRequest.setShift(shift);

        if (serviceRequestDTO.getVeterinarianId() != 0) {
            // Lấy danh sách bác sĩ thú y khả dụng theo ngày hẹn và ca làm việc
            List<Object[]> veterinarians = accountRepository.findAvailableVeterinarians(serviceRequestDTO.getAppointmentTime(), serviceRequestDTO.getShiftId());

            // Kiểm tra nếu có veterinarians và tìm kiếm veterinarianId trong danh sách
            boolean vetExists = veterinarians.stream()
                    .anyMatch(vet -> vet[0].equals(serviceRequestDTO.getVeterinarianId())); // Giả sử vet[0] là ID của bác sĩ

            if (!vetExists) {
                throw new AppException(ErrorCode.VETERINARIAN_NOT_AVAILABLE);
            }

            // Nếu tìm thấy bác sĩ thú y, set bác sĩ cho service request
            Account veterinarian = accountRepository.findById(serviceRequestDTO.getVeterinarianId())
                    .orElseThrow(() -> new AppException(ErrorCode.VETERINARIAN_NOT_EXISTED));

            serviceRequest.setVeterinarian(veterinarian);
        }

        serviceRequestRepository.save(serviceRequest);

        return serviceRequestDTO;
    }

}
