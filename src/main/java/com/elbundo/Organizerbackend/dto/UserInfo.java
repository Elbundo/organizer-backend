package com.elbundo.Organizerbackend.dto;

import com.elbundo.Organizerbackend.models.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfo {
    private Long id;
    private String name;
    private String login;
    private Role role;
    private Long countTasks;
}
