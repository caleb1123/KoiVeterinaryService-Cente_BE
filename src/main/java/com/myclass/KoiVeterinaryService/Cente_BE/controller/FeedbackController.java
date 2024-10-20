package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.BillDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.FeedbackDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateFeedbackRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedback")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class FeedbackController {
    @Autowired
    private FeedbackService feedbackService;

    @PostMapping("/create")
    public ResponseEntity<FeedbackDTO> createFeedback(@RequestBody FeedbackDTO feedbackDTO) {
        FeedbackDTO createdFeedback = feedbackService.createFeedback(feedbackDTO);
        return ResponseEntity.ok(createdFeedback);
    }

    @PostMapping("/update")
    public ResponseEntity<FeedbackDTO> updateFeedback(@RequestBody UpdateFeedbackRequest feedbackDTO) {
        FeedbackDTO updatedFeedback = feedbackService.updateFeedback(feedbackDTO);
        return ResponseEntity.ok(updatedFeedback);
    }

    @GetMapping("/all")
    public ResponseEntity<List<FeedbackDTO>> getAllFeedback() {
        List<FeedbackDTO> feedbacks = feedbackService.getAllFeedback();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/myinfor")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByMyInfor() {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByMyInfor();
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/veterinarian/{veterinarianId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackOfVeterinarianId(@PathVariable int veterinarianId) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackOfVeterinarianId(veterinarianId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/service/{serviceId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByServiceId(@PathVariable int serviceId) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByServiceId(serviceId);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/rating/{ratingValue}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByRatingValue(@PathVariable int ratingValue) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByRatingValue(ratingValue);
        return ResponseEntity.ok(feedbacks);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<FeedbackDTO>> getFeedbackByCustomerId(@PathVariable int customerId) {
        List<FeedbackDTO> feedbacks = feedbackService.getFeedbackByCustomerId(customerId);
        return ResponseEntity.ok(feedbacks);
    }


}
