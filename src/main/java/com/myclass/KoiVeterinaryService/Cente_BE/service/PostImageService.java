package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.PostImageDTO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostImageService {
    String uploadImageToCloudinary(MultipartFile file, int id) throws IOException;

    boolean deleteImage(String fileId) throws IOException;

    PostImageDTO getImageByFileId(String fileId);

    PostImageDTO getImageAuto(int id);

    List<PostImageDTO> getImageByPostId(int postId);

}
