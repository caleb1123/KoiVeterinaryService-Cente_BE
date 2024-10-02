package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback>  findFeedbacksByCustomer_AccountId(int customerId);

    List<Feedback>  findFeedbacksByRatingValue(int ratingValue);

    List<Feedback> findFeedbacksByBill_Service_ServiceId(int serviceId);

    List<Feedback> findFeedbacksByBill_ServiceRequest_Veterinarian_AccountId(int veterinarianId);
}
