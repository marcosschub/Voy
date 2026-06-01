package com.grupo12.Voy.features.users.Mapper;

import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.models.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring")
public interface UserUpdateMapper {
    UserEntity toEntity (UserUpdateDto userUpdateDto);
    UserUpdateDto toDto (UserEntity userEntity);
}
