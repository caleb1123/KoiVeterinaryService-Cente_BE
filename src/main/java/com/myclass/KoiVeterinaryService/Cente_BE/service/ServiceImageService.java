package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ServiceImageService {
    String uploadImageToCloudinary(MultipartFile file, int id) throws IOException;

    ServiceImageDTO getImageByFileId(String fileId);

    ServiceImageDTO getImageAuto(int id);

    boolean deleteImage(String fileId) throws IOException;

    List<ServiceImageDTO> getImageByServiceId(int serviceId);




}
