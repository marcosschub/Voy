package com.grupo12.Voy.features.users.Service;


import com.grupo12.Voy.common.exceptions.EntityDuplicatedException;
import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Dto.UserFollowDto;
import com.grupo12.Voy.features.users.Dto.UserUpdateDto;
import com.grupo12.Voy.features.users.Mapper.NewUserDtoMapper;
import com.grupo12.Voy.features.users.Mapper.UserFollowMapper;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import com.grupo12.Voy.features.users.Mapper.UserUpdateMapper;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import com.grupo12.Voy.features.users.Dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private NewUserDtoMapper newUserDtoMapper;
    private UserFollowMapper userFollowMapper;
    private UserUpdateMapper userUpdateMapper;


    public UserDto findByExternalId(UUID userUuid){
        return userMapper.userToDto(userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado.")));
    }

    public List<UserDto> getAll(){
        return userRepository.findAll().stream().map(userMapper::userToDto).toList();
    }

    public UserDto findByEmail(String userEmail){
        return userMapper.userToDto(userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado")));
    }

    public void deleteUser(Long userId){
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado")));
    }

    public NewUserDto newUser(NewUserDto newUserDto){
       UserEntity user = newUserDtoMapper.newUserToEntity(newUserDto);
        if(userRepository.existsByEmail(newUserDto.email())){
            throw new EntityDuplicatedException("Usuario duplicado");
        }
        userRepository.save(user);
        return newUserDto;
    }

    public UserUpdateDto updateUser(UUID userUuid, UserUpdateDto userUpdateDto){
       UserEntity user =userRepository.findByExternalId(userUuid)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));

       if(userUpdateDto.userName() != null && !userUpdateDto.userName().isBlank()){
           user.setUserName(userUpdateDto.userName());
       }
       if (userUpdateDto.password()!= null && !userUpdateDto.password().isBlank()){
           user.setPassword(userUpdateDto.password());
       }

       return userUpdateMapper.toDto(userRepository.save(user));
    }

    public List<UserFollowDto> listFollowList(UUID userUuid){
          UserEntity  user = userRepository.findByExternalId(userUuid)
                  .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
          return user.getFollowsList().stream().map(userFollowMapper::toDto).toList();
    }

    public List<UserFollowDto> listFollowersList(UUID userUuid){
        UserEntity  user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado"));
        return user.getFollowersList().stream().map(userFollowMapper::toDto).toList();
    }

   /*public List<PartyUserDto> listFollowedParties(UUID userUuid){
        UserEntity user = userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));

        return user.getFollowedParties().stream().map()
    }
   */



    /*
    ver notificaciones

    traer followedparties
    traer myparties
    traer mytickets
    buscar recibo
     */


}