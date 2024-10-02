package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ServiceImage")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int imageId;

    @Column(length = 100, nullable = false)
    private String fileId;

    @Column(length = 100, nullable = false)
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "serviceId")
    private ServiceKoi service;

    @Column
    private boolean status;
}
