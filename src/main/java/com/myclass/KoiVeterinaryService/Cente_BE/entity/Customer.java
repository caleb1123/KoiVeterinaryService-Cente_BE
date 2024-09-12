package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "Customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int accountId;

    @Column
    private String fullName;

    @Column(unique = true)
    private String userName;

    @Column(nullable = false)
    private String password;

    @Column
    private String address;

    @Column
    private String dob;

    @Column
    private String email;

    @Column
    private String phone;




}
