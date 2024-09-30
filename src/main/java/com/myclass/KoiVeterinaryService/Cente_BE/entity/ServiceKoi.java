package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Service")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceKoi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int serviceId;

    @Column(nullable = false)
    private String serviceName;

    @Column(nullable = false)
    private double price;

    @Column
    private String description;

    @Column(nullable = false)
    private String serviceType;

    @Column
    private boolean isActive;

    @OneToMany(mappedBy = "service")
    private List<Bill> bills;
}
