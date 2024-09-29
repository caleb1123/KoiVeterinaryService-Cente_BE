package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceKoi;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<ServiceKoi, Integer> {
    Optional<ServiceKoi> findByServiceName(String serviceName);

    // Kiểm tra xem dịch vụ đã tồn tại hay chưa
    boolean existsByServiceName(String serviceName);

    // Kiểm tra xem dịch vụ có đang hoạt động hay không
    boolean existsByIsActive(boolean isActive);

    void deleteByServiceName(String serviceName);
}