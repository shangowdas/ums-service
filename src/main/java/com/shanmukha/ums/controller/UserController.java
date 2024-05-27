package com.shanmukha.ums.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shanmukha.ums.dto.AddUserRequestDTO;
import com.shanmukha.ums.dto.AddUserResponseDTO;
import com.shanmukha.ums.dto.GetUsersDTO;
import com.shanmukha.ums.dto.GetUsersPagedDTO;
import com.shanmukha.ums.dto.UpdateUserRequestDTO;
import com.shanmukha.ums.dto.UpdateUserResponseDTO;
import com.shanmukha.ums.exception.InternalServerException;
import com.shanmukha.ums.exception.ResourceNotFoundException;
import com.shanmukha.ums.service.UserService;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/user-management/v1/")
@CrossOrigin
@OpenAPIDefinition(info = @Info(title = "Health controller"), tags = @Tag(name = "User Controller"))
public class UserController {
	
	@Autowired
	UserService userService;

	@PostMapping(value = "user")
    @Operation(description = "Creates a new user")
    public ResponseEntity<AddUserResponseDTO> addUser(@Valid @RequestBody AddUserRequestDTO addUserRequestDTO) throws InternalServerException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(addUserRequestDTO));
    }
	
	@PatchMapping(value = "user/{username}")
    @Operation(description = "Updates user by username")
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@RequestBody UpdateUserRequestDTO updateUserRequestDTO, @PathVariable String username) throws ResourceNotFoundException, InternalServerException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(updateUserRequestDTO, username));
    }

    @DeleteMapping(value = "user/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) throws ResourceNotFoundException, InternalServerException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.deleteUser(username));
    }

    @GetMapping(value = "user")
    public ResponseEntity<GetUsersPagedDTO> getUsers(@RequestParam(defaultValue = "10") Integer pageSize, @RequestParam(defaultValue = "0") Integer pageNo, @RequestParam(defaultValue = "id") String sortBy, @RequestParam(name = "name", defaultValue = "") String name, @RequestParam(name = "username", defaultValue = "") String username, @RequestParam(name = "role", defaultValue = "") String role) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(pageSize, pageNo, sortBy, name, username, role));
    }
    
    @GetMapping(value = "user/login/{username}/{password}")
    public ResponseEntity<GetUsersDTO> login(@PathVariable String username, @PathVariable String password) throws ResourceNotFoundException, InternalServerException {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(username, password));
    }
	
}