package com.grupo12.Voy.features.users.Mapper;

import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface UserUpdateMapper {
    @Mapping(source = "password", target = "credentials.password")
    UserEntity toEntity (UserUpdateDto userUpdateDto);

    @Mapping(source = "credentials.password", target = "password")
    UserUpdateDto toDto (UserEntity userEntity);
}
