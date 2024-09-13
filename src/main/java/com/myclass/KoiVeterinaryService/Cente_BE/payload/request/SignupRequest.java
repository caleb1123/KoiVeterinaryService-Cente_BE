package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignupRequest {
    String userName;
    String password;
    String fullName;
    String phone;
    String email;
}
