package com.elbundo.Organizerbackend.dto;

import com.elbundo.Organizerbackend.models.Role;
import com.elbundo.Organizerbackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String accessToken;
}
