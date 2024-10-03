package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostImageDTO {
    private String fileId;
    private String imageUrl;
    private boolean status;
    private int postId;
}
