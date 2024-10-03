package com.myclass.KoiVeterinaryService.Cente_BE.service.impl;

import com.myclass.KoiVeterinaryService.Cente_BE.dto.PostDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Account;
import com.myclass.KoiVeterinaryService.Cente_BE.entity.Post;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.AppException;
import com.myclass.KoiVeterinaryService.Cente_BE.exception.ErrorCode;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.AccountRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.repository.PostRepository;
import com.myclass.KoiVeterinaryService.Cente_BE.service.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    public PostDTO createPost(PostDTO postDTO) {
        Account account = accountRepository.findById(postDTO.getAuthorId())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        Post post = postRepository.getReferenceById(postDTO.getPostId());
        if (post == null) {
            throw new AppException(ErrorCode.POST_EXISTED);
        }

        post = modelMapper.map(postDTO, Post.class);
        post.setAccount(account);
        postRepository.save(post);
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO) {
        Post post = postRepository.getReferenceById(postDTO.getPostId());
        if (post == null) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setStatus(postDTO.isStatus());
        postRepository.save(post);

        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public void deletePost(int id) {
        Post post = postRepository.getReferenceById(id);
        if (post == null) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        post.setStatus(false);
        postRepository.save(post);

    }

    @Override
    public PostDTO findById(int id) {
        Post post = postRepository.getReferenceById(id);
        if (post == null) {
            throw new AppException(ErrorCode.POST_NOT_FOUND);
        }
        return modelMapper.map(post, PostDTO.class);
    }

    @Override
    public List<PostDTO> findAll() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
    }

    @Override
    public List<PostDTO> findByAuthorId(int authorId) {
       List<Post> posts = postRepository.findPostsByAccount_AccountId(authorId);
         return posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
    }

    @Override
    public List<PostDTO> findByTitle(String title) {
        List<Post> posts = postRepository.findAllByTitleContaining(title);
        return posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
    }

    @Override
    public List<PostDTO> findByStatus(boolean status) {
        List<Post> posts = postRepository.findAllByStatus(status);
        return posts.stream().map(post -> modelMapper.map(post, PostDTO.class)).toList();
    }
}
