package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.BlackListToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlackTokenRepository extends JpaRepository<BlackListToken,String> {
}
