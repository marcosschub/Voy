package com.grupo12.Voy.features.users.Service;

import com.grupo12.Voy.features.parties.Dto.PartyUsersDto;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;

import java.util.List;
import java.util.UUID;

public interface IUsersService {
    UserDto findByExternalId(UUID userUuid);
    List<UserDto> getAll();
    UserDto findByEmail(String userEmail);
    void deleteUser(Long userId);
    UserDto newUser(NewUserDto newUserDto);
    UserUpdateDto updateUser(UUID userUuid, UserUpdateDto userUpdateDto);
    List<UserFollowDto> listFollowList(UUID userUuid);
    List<UserFollowDto> listFollowersList(UUID userUuid);
    List<PartyUsersDto> listMyParties(UUID userId);
    List<PartyUsersDto> listFollowedParties(UUID userUuid);
}
