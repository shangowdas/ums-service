package com.shanmukha.ums.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.shanmukha.ums.dto.AddUserRequestDTO;
import com.shanmukha.ums.dto.AddUserResponseDTO;
import com.shanmukha.ums.dto.GetUsersDTO;
import com.shanmukha.ums.dto.GetUsersPagedDTO;
import com.shanmukha.ums.dto.PageDTO;
import com.shanmukha.ums.dto.UpdateUserRequestDTO;
import com.shanmukha.ums.dto.UpdateUserResponseDTO;
import com.shanmukha.ums.exception.ConflictException;
import com.shanmukha.ums.exception.InternalServerException;
import com.shanmukha.ums.exception.ResourceNotFoundException;
import com.shanmukha.ums.mapper.UserManagerMapper;
import com.shanmukha.ums.model.User;
import com.shanmukha.ums.repository.UserManagerRepository;
import com.shanmukha.ums.repository.UserRepository;
import com.shanmukha.ums.service.UserService;

import lombok.NonNull;

@Component
public class UserServiceImpl implements UserService {
	
	@Autowired
	UserManagerMapper mapper;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	UserManagerRepository userManagerRepository;

	@Override
	@Transactional(rollbackFor = Exception.class)
	public AddUserResponseDTO addUser(@NonNull AddUserRequestDTO userAddRequestDTO) throws InternalServerException {
//		log.info("creating user with username: {}", userAddRequestDTO.getUsername());

        try {
            if (userRepository.existsUserByUsername(userAddRequestDTO.getUsername())) {
                String errorMessage = String.format("user with username: %s already exist, please enter a unique username.", userAddRequestDTO.getUsername());
//                log.warn(errorMessage);
                throw new ConflictException(errorMessage);
            }

            User user = mapper.addUserRequestDTOToUser(userAddRequestDTO);

            userRepository.save(user);
//            log.info("successfully created user with username: {}", userAddRequestDTO.getUsername());
            return new AddUserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getRole(), user.getPhone());
        } catch (Exception e) {
            String errorMessage = String.format("error occurred while creating new user with username: %s", userAddRequestDTO.getUsername());
//            log.warn(errorMessage);
            throw new InternalServerException(errorMessage);
        }
	}

	@Override
    @Transactional(rollbackFor = Exception.class)
    public UpdateUserResponseDTO updateUser(@NonNull UpdateUserRequestDTO userUpdateRequestDTO, @NonNull String username) throws ResourceNotFoundException, InternalServerException {
//        log.info("updating user with username: {}", username);
        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Could not find user with username: %s", username);
//                    log.warn(errorMessage);
                    return new ResourceNotFoundException(errorMessage);
                });

        try {
            mapper.updateUserRequestDTOToUser(userUpdateRequestDTO, user);
            userRepository.save(user);
//            log.info("successfully updated user with username: {}", username);

            return new UpdateUserResponseDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getRole(), user.getPhone());
        } catch (Exception e) {
            String errorMessage = String.format("error occurred while updating user with username: %s", username);
//            log.error(errorMessage, e);
            throw new InternalServerException(errorMessage);
        }
    }

	@Override
    public GetUsersPagedDTO getUsers(Integer pageSize, Integer pageNo, String sortBy, String name, String username, String role) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<GetUsersDTO> pageResult = userManagerRepository.getUsers(paging, name, username, role);
        return new GetUsersPagedDTO(pageResult.getContent(), new PageDTO(pageResult.getTotalPages(),
                pageResult.getNumber(), pageResult.getNumberOfElements(), pageResult.getTotalElements()));
    }

	@Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteUser(@NonNull String username) throws ResourceNotFoundException, InternalServerException {
//        log.info("deleting user with username: {}", username);

        User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Could not find user with username: %s", username);
//                    log.warn(errorMessage);
                    return new ResourceNotFoundException(errorMessage);
                });
        try {
            user.setDiscarded(true);
            userRepository.save(user);

            String successMessage = String.format("Successfully deleted user with username: %s", username);
//            log.info(successMessage);
            return successMessage;
        } catch (Exception e) {
            String errorMessage = String.format("error occurred while deleting user with username: %s", username);
//            log.error(errorMessage, e);
            throw new InternalServerException(errorMessage);
        }
    }

	@Override
	public GetUsersDTO login(@NonNull String username, @NonNull String password)
			throws ResourceNotFoundException, InternalServerException {
		User user = userRepository.findUserByUsername(username)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Could not find user with username: %s", username);
//                    log.warn(errorMessage);
                    return new ResourceNotFoundException(errorMessage);
                });
		if (user != null) {
			if (!user.getPassword().equals(password)) {
				String message = String.format("wrong password");
				throw new ResourceNotFoundException(message);
			}
		}else {
			throw new ResourceNotFoundException(String.format("Could not find user with username: %s", username));
		}
		return new GetUsersDTO(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getRole(), user.getPhone());
	}

}
