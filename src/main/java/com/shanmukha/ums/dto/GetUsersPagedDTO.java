package com.shanmukha.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetUsersPagedDTO {
    private List<GetUsersDTO> users;
    private PageDTO page;
}