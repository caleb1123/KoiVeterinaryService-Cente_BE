package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ServiceImageRepository extends JpaRepository<ServiceImage, Integer> {
    @Query(value = "SELECT COUNT(*) AS image_count FROM service_image WHERE service_id = :id", nativeQuery = true)
    Integer getImageCountByServiceid(int id);

    ServiceImage findByFileId(String fileId);

    @Query(value = "SELECT TOP 1 * FROM service_image WHERE service_id = 1 AND status = 1 ORDER BY image_id ASC;", nativeQuery = true)
    ServiceImage findServiceImageAuto(int id);

    @Query(value = "SELECT * FROM service_image WHERE service_id = :id and status = 1", nativeQuery = true)
    List<ServiceImage> findImageByServiceId(int id);
}
