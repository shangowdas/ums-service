package com.shanmukha.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserResponseDTO {
    private Long id;
    private String name;
    private String username;
    private String email;
    private String role;
    private String phone;
}
