package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {
    List<Feedback>  findFeedbacksByCustomer_AccountId(int customerId);

    List<Feedback>  findFeedbacksByRatingValue(int ratingValue);

    @Query(value = "SELECT f.[rating_id],\n" +
            "       f.[comment],\n" +
            "       f.[feedback_type],\n" +
            "       f.[rating_date],\n" +
            "       f.[rating_value],\n" +
            "       f.[customer_id],\n" +
            "       f.[request_id],\n" +
            "       s.[service_id],\n" +
            "       s.[service_name]\n" +
            "FROM [KoiVeterinary].[dbo].[feedback] f\n" +
            "JOIN [KoiVeterinary].[dbo].[service_request] sr\n" +
            "  ON f.[request_id] = sr.[request_id]\n" +
            "JOIN [KoiVeterinary].[dbo].[bill] b\n" +
            "  ON sr.[request_id] = b.[request_id]\n" +
            "JOIN [KoiVeterinary].[dbo].[service] s\n" +
            "  ON b.[service_id] = s.[service_id]\n" +
            "WHERE s.[service_id] = 1;  ", nativeQuery = true)
    List<Feedback> findFeedbacksByServiceId(int serviceId);
    List<Feedback> findFeedbacksByRequest_Veterinarian_AccountId(int veterinarianId);
}
