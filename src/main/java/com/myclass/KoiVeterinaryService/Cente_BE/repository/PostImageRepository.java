package com.myclass.KoiVeterinaryService.Cente_BE.repository;

import com.myclass.KoiVeterinaryService.Cente_BE.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostImageRepository extends JpaRepository<PostImage,Integer> {
    @Query(value = "SELECT COUNT(*) AS image_count FROM post_image WHERE post_id = :id and status = 1", nativeQuery = true)
    Integer getImageCountByPostId(int id);

    PostImage findByFileId(String fileId);

    @Query(value = "SELECT TOP 1 * FROM post_image WHERE post_id = :id AND status = 1 ORDER BY image_id ASC;", nativeQuery = true)
    PostImage findPostImageAuto(int id);

    @Query(value = "SELECT * FROM post_image WHERE post_id = :id and status = 1", nativeQuery = true)
    List<PostImage> findImageByPostId(int id);
}
