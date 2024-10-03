package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PostImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @Column(nullable = false)
    private String fileId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private boolean status;

    @ManyToOne
    @JoinColumn(name = "postId")
    private Post post;

}
