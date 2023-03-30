package com.elbundo.Organizerbackend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String accessToken;
}
