package com.myclass.KoiVeterinaryService.Cente_BE.payload.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateServiceRequest {
    private String serviceName;
    private double price;
    private String description;
    private String serviceType;
    private boolean isActive;
}
