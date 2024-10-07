package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAccountRequest {
    private String fullName;
    private String phone;
    private boolean active;
    private String dob;
    private String email;
    private String address;
}
