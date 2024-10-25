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
    public ResponseEntity<ServiceDTO> updateService(@RequestParam int serviceId, @RequestBody UpdateServiceRequest serviceDTO) {
        ServiceDTO updatedService = serviceService.updateService(serviceDTO,serviceId);
        return ResponseEntity.ok(updatedService);
    }

    // Xóa dịch vụ
    @DeleteMapping("/delete/{serviceId}")
    public ResponseEntity<String> deleteService(@PathVariable int serviceId) {
        serviceService.deleteService(serviceId);
        return ResponseEntity.ok("Delete success");
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

    @GetMapping("/findbytype/{serviceType}")
    public ResponseEntity<List<ServiceDTO>> findServiceByType(@RequestParam String serviceType) {
        List<ServiceDTO> serviceDTOs = serviceService.findServiceByType(serviceType);
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/findbyprice/{price}")
    public ResponseEntity<List<ServiceDTO>> findServiceByPrice(@RequestParam double price) {
        List<ServiceDTO> serviceDTOs = serviceService.findServiceByPrice(price);
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/findbyactive/{active}")
    public ResponseEntity<List<ServiceDTO>> findServiceByActive(@RequestParam boolean active) {
        List<ServiceDTO> serviceDTOs = serviceService.findServiceByActive(active);
        return ResponseEntity.ok(serviceDTOs);
    }

    @GetMapping("/findbyname/{serviceName}")
    public ResponseEntity<List<ServiceDTO>> findServiceByServiceName(@RequestParam String serviceName) {
        List<ServiceDTO> serviceDTOs = serviceService.findServiceByServiceName(serviceName);
        return ResponseEntity.ok(serviceDTOs);
    }
}
