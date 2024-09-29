package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateServiceRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/servicekoi")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class ServiceKoiController {
    @Autowired
    private ServiceService serviceService;

    // Tạo mới dịch vụ
    @PostMapping("/create")
    public ResponseEntity<ServiceDTO> createService(@RequestBody ServiceDTO serviceDTO) {
        ServiceDTO createdService = serviceService.createService(serviceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdService);
    }

    // Cập nhật dịch vụ
    @PutMapping("/update/{serviceId}")
    public ResponseEntity<ServiceDTO> updateService(@RequestParam String serviceName, @RequestBody UpdateServiceRequest serviceDTO) {
        ServiceDTO updatedService = serviceService.updateService(serviceDTO,serviceName);
        return ResponseEntity.ok(updatedService);
    }

    // Xóa dịch vụ
    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<Void> deleteService(@RequestParam String serviceName) {
        serviceService.deleteService(serviceName);
        return ResponseEntity.noContent().build();
    }

    // Tìm dịch vụ theo ID
    @GetMapping("findbyid/{serviceId}")
    public ResponseEntity<ServiceDTO> findById(@RequestParam String serviceName) {
        ServiceDTO serviceDTO = serviceService.findById(serviceName);
        return ResponseEntity.ok(serviceDTO);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ServiceDTO>> findAll() {
        List<ServiceDTO> serviceDTOs = serviceService.findAll();
        return ResponseEntity.ok(serviceDTOs);
    }
}