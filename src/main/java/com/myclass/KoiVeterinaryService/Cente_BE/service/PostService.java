package com.myclass.KoiVeterinaryService.Cente_BE.service;

import com.myclass.KoiVeterinaryService.Cente_BE.dto.PostDTO;

import java.util.List;

public interface PostService {
    PostDTO createPost(PostDTO postDTO);

    PostDTO updatePost(PostDTO postDTO);

    void deletePost(int id);

    PostDTO findById(int id);

    List<PostDTO> findAll();

    List<PostDTO> findByAuthorId(int authorId);

    List<PostDTO> findByTitle(String title);

    List<PostDTO> findByStatus(boolean status);


}
