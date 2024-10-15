package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/serviceimage")
@Slf4j
@CrossOrigin(origins = "http://localhost:5157")
public class ServiceImageController {
    @Autowired
    private ServiceImageService serviceImageService;


    @PostMapping("/upload/{serviceId}")
    public ResponseEntity<String> uploadImageCloudinary(@RequestParam("file") MultipartFile file, @PathVariable int serviceId) throws IOException {
        try {

            var imageUrl= serviceImageService.uploadImageToCloudinary(file,serviceId);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @GetMapping("/list/{serviceId}")
    public ResponseEntity<?> getImageByServiceId(@PathVariable int serviceId) {
        try {
            return ResponseEntity.ok(serviceImageService.getImageByServiceId(serviceId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteImage(@PathVariable String fileId) {
        try {
            boolean isDeleted = serviceImageService.deleteImage(fileId);
            if (isDeleted) {
                return ResponseEntity.ok("Image deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image: " + e.getMessage());
        }
    }

    @GetMapping("/auto/{serviceId}")
    public ResponseEntity<?> getImageAuto(@PathVariable int serviceId) {
        try {
            return ResponseEntity.ok(serviceImageService.getImageAuto(serviceId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }

    @GetMapping("/{fileId}")
    public ResponseEntity<?> getImageByFileId(@PathVariable String fileId) {
        try {
            return ResponseEntity.ok(serviceImageService.getImageByFileId(fileId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }


}
