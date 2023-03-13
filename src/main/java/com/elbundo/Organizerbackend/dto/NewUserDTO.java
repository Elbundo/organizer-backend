package com.elbundo.Organizerbackend.dto;

import com.elbundo.Organizerbackend.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewUserDTO {
    private User user;
    private String password;
}