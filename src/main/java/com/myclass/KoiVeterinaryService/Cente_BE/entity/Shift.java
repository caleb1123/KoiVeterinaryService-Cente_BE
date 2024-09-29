package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Shift")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int shiftId;

    @Enumerated(EnumType.STRING) // Store enum as a string in the database
    @Column(nullable = false, unique = true)
    private ShiftName shiftName; // Change from String to ShiftName

    @OneToMany(mappedBy = "shift")
    private List<ServiceRequest> serviceRequests;
}
