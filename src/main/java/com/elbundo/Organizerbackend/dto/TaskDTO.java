package com.elbundo.Organizerbackend.dto;

import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String text;
    private boolean status;
}
