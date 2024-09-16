package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAccountRequest {
    private String fullName;
    private String userName;
    private String password;
    private String phone;
    private String email;
    private int rodeId;
}
