package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @Enumerated(EnumType.STRING) // Lưu giá trị enum dưới dạng chuỗi
    @Column(length = 20, nullable = false) // Đảm bảo cột này không được phép null
    private ERole roleName;

    @OneToMany(mappedBy = "role")
    private List<Account> accounts;
}
