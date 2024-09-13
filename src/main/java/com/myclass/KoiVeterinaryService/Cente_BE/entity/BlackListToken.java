package com.myclass.KoiVeterinaryService.Cente_BE.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlackListToken {
    @Id
    String id;
    Date expiryTime;
}
