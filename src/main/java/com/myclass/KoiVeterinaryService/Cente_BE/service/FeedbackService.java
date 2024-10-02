package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.FeedbackDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.request.UpdateFeedbackRequest;

import java.util.List;

public interface FeedbackService {
    FeedbackDTO createFeedback(FeedbackDTO feedbackDTO);

    FeedbackDTO updateFeedback(UpdateFeedbackRequest feedbackDTO);

    List<FeedbackDTO> getAllFeedback();

    List<FeedbackDTO> getFeedbackByMyInfor();

    List<FeedbackDTO> getFeedbackOfVeterinarianId(int veterinarianId);

    List<FeedbackDTO> getFeedbackByServiceId(int serviceId);

    List<FeedbackDTO> getFeedbackByRatingValue(int ratingValue);

    List<FeedbackDTO> getFeedbackByCustomerId(int customerId);
}
