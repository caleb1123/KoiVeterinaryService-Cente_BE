package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.*;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.FeedbackDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateFeedbackRequest;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackService implements com.myclass.KoiVeterinaryService.Cente_BE.service.FeedbackService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    FeedbackRepository feedbackRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    ServiceRequestRepository serviceRequestRepository;
    @Autowired
    ServiceKoiRepository serviceKoiRepository;
    @Autowired
    BillRepository billRepository;

    @Override
    public FeedbackDTO createFeedback(FeedbackDTO feedbackDTO) {
        Account customer = accountRepository.findById(feedbackDTO.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        ServiceRequest serviceRequest = serviceRequestRepository.findById(feedbackDTO.getRequestId())
                .orElseThrow(() -> new RuntimeException("Service request not found"));
        if(serviceRequest.getStatus() != EStatus.COMPLETED) {
            throw new RuntimeException("Service request is not completed");
        }

        if(feedbackDTO.getRatingValue() > 5 && feedbackDTO.getRatingValue() < 0) {
            throw new RuntimeException("Rating value must be between 0 and 5");
        }
        FeedbackType feedbackType = FeedbackType.valueOf(feedbackDTO.getFeedbackType());
        Feedback feedback = modelMapper.map(feedbackDTO, Feedback.class);
        feedback.setCustomer(customer);
        feedback.setRequest(serviceRequest);
        feedbackRepository.save(feedback);
        return modelMapper.map(feedbackRepository.save(feedback), FeedbackDTO.class);
    }

    @Override
    public FeedbackDTO updateFeedback(UpdateFeedbackRequest feedbackDTO) {
        Feedback feedback = feedbackRepository.findById(feedbackDTO.getFeedbackId())
                .orElseThrow(() -> new RuntimeException("Feedback not found"));
        feedback.setRatingValue(feedbackDTO.getRatingValue());
        feedback.setComment(feedbackDTO.getComment());
        feedback.setRatingDate(feedbackDTO.getRatingDate());
        feedbackRepository.save(feedback);
        return modelMapper.map(feedbackRepository.save(feedback), FeedbackDTO.class);
    }

    @Override
    public List<FeedbackDTO> getAllFeedback() {
        List<Feedback> feedbacks = feedbackRepository.findAll();
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByMyInfor() {
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();
        Account byUserName = accountRepository.findByUserName(name)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByCustomer_AccountId(byUserName.getAccountId());
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }

    @Override
    public List<FeedbackDTO> getFeedbackOfVeterinarianId(int veterinarianId) {
        Account veterinarian = accountRepository.findById(veterinarianId)
                .orElseThrow(() -> new RuntimeException("Veterinarian not found"));
        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByVeterinarianAndType(veterinarian.getAccountId());
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByServiceId(int serviceId) {
        ServiceKoi serviceKoi = serviceKoiRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Service not found"));
        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByServiceId(serviceKoi.getServiceId());
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByRatingValue(int ratingValue) {
        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByRatingValue(ratingValue);
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }

    @Override
    public List<FeedbackDTO> getFeedbackByCustomerId(int customerId) {
        Account customer = accountRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        List<Feedback> feedbacks = feedbackRepository.findFeedbacksByCustomer_AccountId(customer.getAccountId());
        return feedbacks.stream().map(feedback -> modelMapper.map(feedback, FeedbackDTO.class)).toList();
    }


}
