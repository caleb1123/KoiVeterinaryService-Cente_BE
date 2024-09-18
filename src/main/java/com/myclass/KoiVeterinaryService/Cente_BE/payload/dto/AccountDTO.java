package com.myclass.KoiVeterinaryService.Cente_BE.payload.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountDTO {
    private int accountId;
    private String fullName;
    private String userName;
    private String phone;
    private boolean active;
    private String dob;
    private String email;
    private String address;
    private int roleId;
}
