package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.service.PostImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/post-image")
@Slf4j
@CrossOrigin(origins = "http://localhost:5153")
public class PostImageController {
    @Autowired
    private PostImageService postImageService;

    @PostMapping("/upload/{postId}")
    public ResponseEntity<String> uploadImageCloudinary(@RequestParam("file") MultipartFile file, @PathVariable int postId) throws IOException {
        try {

            var imageUrl= postImageService.uploadImageToCloudinary(file,postId);
            return ResponseEntity.ok(imageUrl);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{fileId}")
    public ResponseEntity<String> deleteImage(@PathVariable String fileId) {
        try {
            boolean isDeleted = postImageService.deleteImage(fileId);
            if (isDeleted) {
                return ResponseEntity.ok("Image deleted successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found");
            }
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete image: " + e.getMessage());
        }
    }

    @GetMapping("/auto/{postId}")
    public ResponseEntity<?> getImageAuto(@PathVariable int postId) {
        try {
            return ResponseEntity.ok(postImageService.getImageAuto(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }

    @GetMapping("/list/{postId}")
    public ResponseEntity<?> getImageByPostId(@PathVariable int postId) {
        try {
            return ResponseEntity.ok(postImageService.getImageByPostId(postId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }

    @GetMapping("/fileId/{fileId}")
    public ResponseEntity<?> getImageByFileId(@PathVariable String fileId) {
        try {
            return ResponseEntity.ok(postImageService.getImageByFileId(fileId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get image: " + e.getMessage());
        }
    }


}
