package com.elbundo.Organizerbackend.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String refreshToken;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private User user;
}
