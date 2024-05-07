package com.shanmukha.ums.service;

import org.springframework.stereotype.Service;

import com.shanmukha.ums.dto.AddUserRequestDTO;
import com.shanmukha.ums.dto.AddUserResponseDTO;
import com.shanmukha.ums.dto.GetUsersPagedDTO;
import com.shanmukha.ums.dto.UpdateUserRequestDTO;
import com.shanmukha.ums.dto.UpdateUserResponseDTO;
import com.shanmukha.ums.exception.InternalServerException;
import com.shanmukha.ums.exception.ResourceNotFoundException;

import lombok.NonNull;

@Service
public interface UserService {

	AddUserResponseDTO addUser(@NonNull AddUserRequestDTO userAddRequestDTO) throws InternalServerException;

    UpdateUserResponseDTO updateUser(@NonNull UpdateUserRequestDTO userUpdateRequestDTO, @NonNull String username) throws ResourceNotFoundException, InternalServerException;

    GetUsersPagedDTO getUsers(Integer pageSize, Integer pageNo, String sortBy, String name, String username, String role);

    String deleteUser(@NonNull String username) throws ResourceNotFoundException, InternalServerException;
}
