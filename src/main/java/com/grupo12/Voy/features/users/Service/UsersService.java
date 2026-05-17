package com.grupo12.Voy.features.users.Service;

import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.features.users.Dto.NewUserDto;
import com.grupo12.Voy.features.users.Mapper.NewUserDtoMapper;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import com.grupo12.Voy.features.users.Dto.UserDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UsersService {
    private final UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private NewUserDtoMapper newUserDtoMapper;

    public UserDto findById(Long usersId){
       return userMapper.userToDto(userRepository.findById(usersId)
                        .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado.")));
    }

    public UserDto findByExternalId(UUID userUuid){
        return userRepository.findByExternalId(userUuid)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado."));
    }

    public List<UserDto> getAll(){
        return userRepository.findAll().stream().map(userMapper::userToDto).toList();
    }

    public UserDto findByEmail(String userEmail){
        return userRepository.findByEmail(userEmail)
                .orElseThrow(()-> new EntityNotFoundException("Usuario no encontrado"));
    }

    public void deleteUser(Long userId){
        userRepository.delete(userRepository.findById(userId)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado")));
    }

    public NewUserDto newUser(NewUserDto newUserDto){
       UserEntity user = newUserDtoMapper.newUserToEntity(newUserDto);
        if(userRepository.findAll()
                .stream()
                .anyMatch(x->x.getEmail().equals(user.getEmail()))){
            throw new  EntityDuplicatedException("Usuario duplicado");
        }
        userRepository.save(user);
        return newUserDto;
    }

    public UserDto updateUser(UUID userUuid,UserDto userDto){
       UserEntity user = userMapper.userToEntity(userRepository.findByExternalId(userUuid)
                .orElseThrow(()->new EntityNotFoundException("Usuario no encontrado")));

       if(userDto.userName() != null && !userDto.userName().isBlank()){
           user.setUserName(userDto.userName());
       }
       if (userDto.password()!= null && !userDto.password().isBlank()){
           user.setPassword(userDto.password());
       }

       return userMapper.userToDto(userRepository.save(user));
    }


}