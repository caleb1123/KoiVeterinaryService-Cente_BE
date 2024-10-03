package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post,Integer> {
    List<Post> findPostsByAccount_AccountId(int authorId);

    List<Post> findAllByTitleContaining(String title);

    List<Post> findAllByStatus(boolean status);
}
