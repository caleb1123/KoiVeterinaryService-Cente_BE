package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceImage;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.ServiceKoi;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.ServiceImageDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceImageRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.ServiceKoiRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.ServiceImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;


@Service
public class ServiceImagerServiceImpl implements ServiceImageService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    ServiceImageRepository serviceImageRepository;
    @Autowired
    ServiceKoiRepository serviceKoiRepository;
    @Autowired
    Cloudinary cloudinary;
    @Override
    public String uploadImageToCloudinary(MultipartFile file, int id) throws IOException {
        ServiceKoi serviceKoi = serviceKoiRepository.findById(id).get();
        Integer imageCount = serviceImageRepository.getImageCountByServiceid(id);
        if(imageCount == null){
            imageCount = 0;
        }
        if ( imageCount >= 5) {
            throw new AppException(ErrorCode.IMAGE_MANY);
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        ServiceImage serviceImage = new ServiceImage();
        serviceImage.setService(serviceKoi);
        serviceImage.setImageUrl((String) uploadResult.get("url"));
        serviceImage.setStatus(true);
        serviceImage.setFileId(getRandomNumber(8));
        serviceImageRepository.save(serviceImage);
        return (String) uploadResult.get("url");
    }

    @Override
    public ServiceImageDTO getImageByFileId(String fileId) {
        ServiceImage serviceImage = serviceImageRepository.findByFileId(fileId);
        return modelMapper.map(serviceImage, ServiceImageDTO.class);
    }

    @Override
    public ServiceImageDTO getImageAuto(int id) {
        ServiceImage serviceImage = serviceImageRepository.findServiceImageAuto(id);
        return modelMapper.map(serviceImage, ServiceImageDTO.class);
    }

    @Override
    public boolean deleteImage(String fileId) throws IOException {
        ServiceImage serviceImage = serviceImageRepository.findByFileId(fileId);
        if (serviceImage == null) {
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        cloudinary.uploader().destroy(serviceImage.getFileId(), ObjectUtils.emptyMap());
        serviceImage.setStatus(false);
        serviceImageRepository.save(serviceImage);
        return true;
    }

    @Override
    public List<ServiceImageDTO> getImageByServiceId(int serviceId) {
        List<ServiceImage> list = serviceImageRepository.findImageByServiceId(serviceId);
        return list.stream().map(serviceImage -> modelMapper.map(serviceImage, ServiceImageDTO.class)).toList();
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
