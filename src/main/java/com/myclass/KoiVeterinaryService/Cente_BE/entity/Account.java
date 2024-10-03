package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Account")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

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
    private String phone;

    @Column
    private boolean active;

    @Column
    private String dob;

    @Column
    private String email;

    @Column
    private String address;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // Map the relationship with Payment entity
    @OneToMany(mappedBy = "account")
    private List<Payment> payments;

    // Map the relationship with ServiceRequest entity
    @OneToMany(mappedBy = "customer")
    private List<ServiceRequest> serviceRequests;

    // Map the relationship with Feedback entity
    @OneToMany(mappedBy = "customer")
    private List<Feedback> feedbacks;

    // Map the relationship with Post entity
    @OneToMany(mappedBy = "account")
    private List<Post> posts;

}
