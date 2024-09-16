package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.AccessLevel;
import lombok.*;
import lombok.experimental.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {
    String userName;
    String password;
}
