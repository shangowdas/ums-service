package com.shanmukha.ums.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.shanmukha.ums.dto.AddUserRequestDTO;
import com.shanmukha.ums.dto.UpdateUserRequestDTO;
import com.shanmukha.ums.model.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserManagerMapper {

    User addUserRequestDTOToUser(AddUserRequestDTO userAddRequestDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserRequestDTOToUser(UpdateUserRequestDTO userUpdateRequestDTO, @MappingTarget User user);

}
