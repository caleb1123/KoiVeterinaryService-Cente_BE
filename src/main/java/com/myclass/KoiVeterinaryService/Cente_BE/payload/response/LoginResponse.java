package com.myclass.KoiVeterinaryService.Cente_BE.payload.response;

import com.myclass.KoiVeterinaryService.Cente_BE.payload.dto.AccountDTO;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginResponse {
    String token;
    boolean authenticated;
    AccountDTO account;

}
