package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Post;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.PostImage;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.PostImageDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.PostImageRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.PostRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.PostImageService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class PostImageServiceImpl implements PostImageService {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    PostImageRepository postImageRepository;
    @Autowired
    PostRepository postRepository;
    @Autowired
    Cloudinary cloudinary;
    @Override
    public String uploadImageToCloudinary(MultipartFile file, int id) throws IOException {
        Post post = postRepository.findById(id)
                .orElseThrow( () -> new AppException(ErrorCode.POST_NOT_FOUND));
        int count = postImageRepository.getImageCountByPostId(id);
        if(count >= 5){
            throw new AppException(ErrorCode.IMAGE_MANY);
        }
        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        PostImage postImage = new PostImage();
        postImage.setPost(post);
        postImage.setImageUrl((String) uploadResult.get("url"));
        postImage.setImageUrl((String) uploadResult.get("url"));
        postImage.setStatus(true);
        postImage.setFileId(getRandomNumber(8));
        postImageRepository.save(postImage);
        return (String) uploadResult.get("url");
    }

    @Override
    public boolean deleteImage(String fileId) throws IOException {
        PostImage postImage = postImageRepository.findByFileId(fileId);
        if(postImage == null){
            throw new AppException(ErrorCode.IMAGE_NOT_FOUND);
        }
        Map result = cloudinary.uploader().destroy(fileId,ObjectUtils.emptyMap());
        postImage.setStatus(false);
        postImageRepository.save(postImage);
        return true;
    }

    @Override
    public PostImageDTO getImageByFileId(String fileId) {
        PostImage postImage = postImageRepository.findByFileId(fileId);
        return modelMapper.map(postImage, PostImageDTO.class);
    }

    @Override
    public PostImageDTO getImageAuto(int id) {
        PostImage postImage = postImageRepository.findPostImageAuto(id);
        return modelMapper.map(postImage, PostImageDTO.class);
    }

    @Override
    public List<PostImageDTO> getImageByPostId(int postId) {
        List<PostImage> postImages = postImageRepository.findImageByPostId(postId);
        return postImages.stream().map(postImage -> modelMapper.map(postImage, PostImageDTO.class)).toList();

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
