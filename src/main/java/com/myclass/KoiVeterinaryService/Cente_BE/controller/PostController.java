package com.myclass.KoiVeterinaryService.Cente_BE.controller;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.PostDTO;
import com.myclass.KoiVeterinaryService.Cente_BE.service.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.createPost(postDTO);
        return ResponseEntity.ok(createdPost);
    }

    @PostMapping("/update")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.updatePost(postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @GetMapping("/all")
    public ResponseEntity<List<PostDTO>> getAllPost() {
        List<PostDTO> posts = postService.findAll();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<List<PostDTO>> getPostByAuthorId(@PathVariable int authorId) {
        List<PostDTO> posts = postService.findByAuthorId(authorId);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/title/{title}")
    public ResponseEntity<List<PostDTO>> getPostByTitle(@PathVariable String title) {
        List<PostDTO> posts = postService.findByTitle(title);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<PostDTO>> getPostByStatus(@PathVariable boolean status) {
        List<PostDTO> posts = postService.findByStatus(status);
        return ResponseEntity.ok(posts);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable int id) {
        postService.deletePost(id);
        return ResponseEntity.ok("Post deleted successfully");
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable int id) {
        PostDTO post = postService.findById(id);
        return ResponseEntity.ok(post);
    }



}
