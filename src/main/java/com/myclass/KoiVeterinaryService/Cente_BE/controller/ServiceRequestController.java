package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.CreateServiceRequestDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/servicekoi")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceRequestController {
    @Autowired
    private ServiceRequestService serviceRequestService;

    @PostMapping("/appointments")
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
}
