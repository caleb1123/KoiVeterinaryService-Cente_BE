package com.myclass.KoiVeterinaryService.Cente_BE.payload.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvailableVeterinariansResponse {
    int accountId;
    String fullName;
    String phone;
    String email;
}
