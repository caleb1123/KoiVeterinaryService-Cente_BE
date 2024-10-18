package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.response.HistoryResponse;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicerequest")
@Slf4j
@CrossOrigin(origins = "http://localhost:5173")
public class ServiceRequestController {
    @Autowired
    private ServiceRequestService serviceRequestService;

    @PostMapping("/appointments/create")
    public ResponseEntity<CreateServiceRequestDTO> createVetAppointment(@RequestBody CreateServiceRequestDTO serviceRequestDTO) {
        CreateServiceRequestDTO createdServiceRequest1 = serviceRequestService.createVetAppointmentService(serviceRequestDTO);
        return new ResponseEntity<>(createdServiceRequest1, HttpStatus.CREATED);
    }

    @PutMapping("/appointments/completed/{serviceRequestId}")
    public ResponseEntity<ServiceRequestDTO> markServiceRequestAsCompleted(@RequestParam Integer serviceRequestId) {
        ServiceRequestDTO serviceRequestDTO = serviceRequestService.markServiceRequestAsCompleted(serviceRequestId);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @PutMapping("/appointments/cancelled/{serviceRequestId}")
    public ResponseEntity<ServiceRequestDTO> markServiceRequestAsCANCELLED(@RequestParam Integer serviceRequestId) {
        ServiceRequestDTO serviceRequestDTO = serviceRequestService.markServiceRequestAsCANCELLED(serviceRequestId);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @PutMapping("/appointments/inprogress/{serviceRequestId}")
    public ResponseEntity<ServiceRequestDTO> markServiceRequestAsInProgress(@RequestParam Integer serviceRequestId) {
        ServiceRequestDTO serviceRequestDTO = serviceRequestService.markServiceRequestAsInProgress(serviceRequestId);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/appointments")
    public ResponseEntity<List<ServiceRequestDTO>> getAllServiceRequest() {
        List<ServiceRequestDTO> serviceRequestDTO = serviceRequestService.getAllServiceRequest();
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/appointments/customer/{customerId}")
    public ResponseEntity<List<HistoryResponse>> getServiceRequestByCustomerId(@RequestParam int customerId) {
        List<HistoryResponse> serviceRequestDTO = serviceRequestService.getServiceRequestByCustomerId(customerId);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/appointments/veterinarian/{veterinarianId}")
    public ResponseEntity<List<ServiceRequestDTO>> getServiceRequestByVeterinarianId(@RequestParam int veterinarianId) {
        List<ServiceRequestDTO> serviceRequestDTO = serviceRequestService.getServiceRequestByVeterinarianId(veterinarianId);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/appointments/status/{status}")
    public ResponseEntity<List<ServiceRequestDTO>> getServiceRequestByStatus(@RequestParam String status) {
        List<ServiceRequestDTO> serviceRequestDTO = serviceRequestService.getServiceRequestByStatus(status);
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }

    @GetMapping("/appointments/myinfor")
    public ResponseEntity<List<ServiceRequestDTO>> getServiceRequestByMyInfor() {
        List<ServiceRequestDTO> serviceRequestDTO = serviceRequestService.getServiceRequestByMyInfor();
        return new ResponseEntity<>(serviceRequestDTO, HttpStatus.OK);
    }


}
