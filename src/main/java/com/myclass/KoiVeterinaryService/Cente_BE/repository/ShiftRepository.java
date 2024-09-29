package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    Optional<Shift> findByShiftName(String shiftName);

    boolean existsByShiftName(String shiftName);

    void deleteByShiftName(String shiftName);
}
