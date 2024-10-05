package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {

    private int postId;

    private String title;

    private String content;

    private int authorId; // Instead of the Account entity, you can use the author's ID.

    private boolean status;

}
